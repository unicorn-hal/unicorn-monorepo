package com.unicorn.api.infrastructure.hospital_news

import com.unicorn.api.domain.hospital_news.HospitalNews
import com.unicorn.api.domain.hospital_news.HospitalNewsID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/primary_doctor/Insert_Hospital_Data.sql")
@Sql("/db/hospital_news/Insert_HospitalNews_data.sql")
class HospitalNewsRepositoryTest {
    @Autowired
    private lateinit var hospitalNewsRepository: HospitalNewsRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findHospitalNewsByHospitalNewsID(hospitalNewsID: HospitalNewsID): HospitalNews? {
        // language=postgresql
        val sql =
            """
            SELECT
                hospital_news_id,
                hospital_id,
                title,
                contents,
                notice_image_url,
                related_url,
                posted_date
            FROM hospital_news
            WHERE hospital_news_id = :hospitalNewsID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("hospitalNewsID", hospitalNewsID.value)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams,
        ) { rs, _ ->
            val postedDate: OffsetDateTime = rs.getObject("posted_date", OffsetDateTime::class.java)

            val jstPostedDate = postedDate.withOffsetSameInstant(ZoneOffset.ofHours(9))

            HospitalNews.fromStore(
                hospitalNewsID = UUID.fromString(rs.getString("hospital_news_id")),
                hospitalID = UUID.fromString(rs.getString("hospital_id")),
                title = rs.getString("title"),
                contents = rs.getString("contents"),
                noticeImageUrl = rs.getString("notice_image_url"),
                relatedUrl = rs.getString("related_url"),
                postedDate = jstPostedDate,
            )
        }.singleOrNull()
    }

    @Test
    fun `should store hospital news`() {
        val hospitalNews =
            HospitalNews.create(
                hospitalID = UUID.fromString("762a7a7e-41e4-46c2-b36c-f2b302cae3e7"),
                title = "本日のお知らせ",
                contents = "本日は臨時休業日です。申し訳ございません。",
                noticeImageUrl = "http://example.com/online_service.jpg",
                relatedUrl = "https://example.com/store",
            )

        hospitalNewsRepository.store(hospitalNews)

        val storeHospitalNews = findHospitalNewsByHospitalNewsID(hospitalNews.hospitalNewsID)

        assert(storeHospitalNews?.hospitalNewsID == hospitalNews.hospitalNewsID)
        assert(storeHospitalNews?.hospitalID == hospitalNews.hospitalID)
        assert(storeHospitalNews?.title == hospitalNews.title)
        assert(storeHospitalNews?.contents == hospitalNews.contents)
        assert(storeHospitalNews?.noticeImageUrl == hospitalNews.noticeImageUrl)
        assert(storeHospitalNews?.relatedUrl == hospitalNews.relatedUrl)
    }

    @Test
    fun `should store hospital news when notice image url is null`() {
        val hospitalNews =
            HospitalNews.create(
                hospitalID = UUID.fromString("762a7a7e-41e4-46c2-b36c-f2b302cae3e7"),
                title = "本日のお知らせ",
                contents = "本日は臨時休業日です。申し訳ございません。",
                noticeImageUrl = "",
                relatedUrl = "https://example.com/store",
            )

        hospitalNewsRepository.store(hospitalNews)

        val storeHospitalNews = findHospitalNewsByHospitalNewsID(hospitalNews.hospitalNewsID)

        assert(storeHospitalNews?.hospitalNewsID == hospitalNews.hospitalNewsID)
        assert(storeHospitalNews?.hospitalID == hospitalNews.hospitalID)
        assert(storeHospitalNews?.title == hospitalNews.title)
        assert(storeHospitalNews?.contents == hospitalNews.contents)
        assert(storeHospitalNews?.noticeImageUrl == hospitalNews.noticeImageUrl)
        assert(storeHospitalNews?.relatedUrl == hospitalNews.relatedUrl)
    }

    @Test
    fun `should store hospital news when related url is null`() {
        val hospitalNews =
            HospitalNews.create(
                hospitalID = UUID.fromString("762a7a7e-41e4-46c2-b36c-f2b302cae3e7"),
                title = "本日のお知らせ",
                contents = "本日は臨時休業日です。申し訳ございません。",
                noticeImageUrl = "http://example.com/online_service.jpg",
                relatedUrl = "",
            )

        hospitalNewsRepository.store(hospitalNews)

        val storeHospitalNews = findHospitalNewsByHospitalNewsID(hospitalNews.hospitalNewsID)

        assert(storeHospitalNews?.hospitalNewsID == hospitalNews.hospitalNewsID)
        assert(storeHospitalNews?.hospitalID == hospitalNews.hospitalID)
        assert(storeHospitalNews?.title == hospitalNews.title)
        assert(storeHospitalNews?.contents == hospitalNews.contents)
        assert(storeHospitalNews?.noticeImageUrl == hospitalNews.noticeImageUrl)
        assert(storeHospitalNews?.relatedUrl == hospitalNews.relatedUrl)
    }

    @Test
    fun `should get hospital news by hospitalNewsID`() {
        val hospitalNewsID = HospitalNewsID(UUID.fromString("d1b2f9d0-9d9b-4625-bc38-54b75d3a0127"))

        val hospitalNews = hospitalNewsRepository.getOrNullBy(hospitalNewsID)

        assert(hospitalNews != null)
        assert(hospitalNews!!.hospitalNewsID.value.toString() == "d1b2f9d0-9d9b-4625-bc38-54b75d3a0127")
        assert(hospitalNews.hospitalID.value.toString() == "d8bfa31d-54b9-4c64-a499-6c522517e5f7")
        assert(hospitalNews.title.value == "新しい診療時間のお知らせ")
        assert(hospitalNews.contents.value == "新しい診療時間は月曜日から金曜日の午前9時から午後5時までです。")
        assert(hospitalNews.noticeImageUrl?.value == "http://example.com/notice_image.png")
        assert(hospitalNews.relatedUrl?.value == "https://example.com/new-schedule")
        val expectedDate = OffsetDateTime.parse("2024-11-29T10:00:00z")
        assert(hospitalNews.postedDate.value == expectedDate)
    }

    @Test
    fun `should return null when hospital news does not exist`() {
        val hospitalNewsID = HospitalNewsID(UUID.fromString("a1b2f9d0-9d9b-4625-bc38-54b75d3a0127"))

        val hospitalNews = hospitalNewsRepository.getOrNullBy(hospitalNewsID)

        assert(hospitalNews == null)
    }

    @Test
    fun `should delete hospital news`() {
        val hospitalNewsID = UUID.fromString("d1b2f9d0-9d9b-4625-bc38-54b75d3a0127")

        val hospitalNews =
            HospitalNews.fromStore(
                hospitalNewsID = hospitalNewsID,
                hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
                title = "新しい診療時間のお知らせ",
                contents = "新しい診療時間は月曜日から金曜日の午前9時から午後5時までです。",
                noticeImageUrl = "http://example.com/notice_image.png",
                relatedUrl = "https://example.com/new-schedule",
                postedDate = OffsetDateTime.of(2024, 11, 29, 10, 0, 0, 0, ZoneOffset.UTC),
            )

        hospitalNewsRepository.delete(hospitalNews)

        val deleteHospitalNews = findHospitalNewsByHospitalNewsID(HospitalNewsID(hospitalNewsID))
        assertEquals(null, deleteHospitalNews)
    }
}
