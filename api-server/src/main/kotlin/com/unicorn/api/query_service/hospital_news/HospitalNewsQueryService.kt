package com.unicorn.api.query_service.hospital_news

import com.fasterxml.jackson.annotation.JsonFormat
import com.unicorn.api.domain.hospital.HospitalID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

interface HospitalNewsQueryService {
    fun getByHospitalID(hospitalID: HospitalID): HospitalNewsResult

    fun getAll(): HospitalNewsResult
}

@Service
class HospitalNewsQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : HospitalNewsQueryService {
    override fun getByHospitalID(hospitalID: HospitalID): HospitalNewsResult {
        val sql =
            """
            SELECT
                hn.hospital_news_id,
                hn.hospital_id,
                h.hospital_name,
                hn.title,
                hn.contents,
                hn.notice_image_url,
                hn.related_url,
                hn.posted_date
            FROM hospital_news hn
            JOIN hospitals h ON hn.hospital_id = h.hospital_id
            WHERE hn.hospital_id = :hospital_id
            AND hn.deleted_at IS NULL
            AND h.deleted_at IS NULL
            ORDER BY hn.posted_date ASC;
            """.trimIndent()
        val sqlParams =
            MapSqlParameterSource()
                .addValue("hospital_id", hospitalID.value)
        val hospitalNews =
            namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
                HospitalNewsDto(
                    hospitalNewsID = UUID.fromString(rs.getString("hospital_news_id")),
                    hospitalID = UUID.fromString(rs.getString("hospital_id")),
                    hospitalName = rs.getString("hospital_name"),
                    title = rs.getString("title"),
                    contents = rs.getString("contents"),
                    noticeImageUrl = rs.getString("notice_image_url"),
                    relatedUrl = rs.getString("related_url"),
                    postedDate = rs.getObject("posted_date", OffsetDateTime::class.java),
                )
            }
        return HospitalNewsResult(hospitalNews)
    }

    override fun getAll(): HospitalNewsResult {
        val sql =
            """
            SELECT
                hn.hospital_news_id,
                hn.hospital_id,
                h.hospital_name,
                hn.title,
                hn.contents,
                hn.notice_image_url,
                hn.related_url,
                hn.posted_date
            FROM hospital_news hn
            JOIN hospitals h ON hn.hospital_id = h.hospital_id
            WHERE hn.deleted_at IS NULL
            AND h.deleted_at IS NULL
            ORDER BY hn.posted_date ASC;
            """.trimIndent()
        val sqlParams =
            MapSqlParameterSource()
        val hospitalNews =
            namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
                HospitalNewsDto(
                    hospitalNewsID = UUID.fromString(rs.getString("hospital_news_id")),
                    hospitalID = UUID.fromString(rs.getString("hospital_id")),
                    hospitalName = rs.getString("hospital_name"),
                    title = rs.getString("title"),
                    contents = rs.getString("contents"),
                    noticeImageUrl = rs.getString("notice_image_url"),
                    relatedUrl = rs.getString("related_url"),
                    postedDate = rs.getObject("posted_date", OffsetDateTime::class.java),
                )
            }
        return HospitalNewsResult(hospitalNews)
    }
}

data class HospitalNewsDto(
    val hospitalNewsID: UUID,
    val hospitalID: UUID,
    val hospitalName: String,
    val title: String,
    val contents: String,
    val noticeImageUrl: String?,
    val relatedUrl: String?,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    var postedDate: OffsetDateTime,
) {
    init {
        val jstOffset = ZoneOffset.ofHours(9)
        postedDate = postedDate.withOffsetSameInstant(jstOffset)
    }
}

data class HospitalNewsResult(
    val data: List<HospitalNewsDto>,
)
