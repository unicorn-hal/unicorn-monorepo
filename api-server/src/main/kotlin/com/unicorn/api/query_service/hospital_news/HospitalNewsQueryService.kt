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
    fun getByHospitalID(hospitalID: HospitalID): HospitalNewsResultToAdmin
}

@Service
class HospitalNewsQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : HospitalNewsQueryService {
    override fun getByHospitalID(hospitalID: HospitalID): HospitalNewsResultToAdmin {
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
            WHERE hospital_id = :hospital_id
            AND deleted_at IS NULL
            ORDER BY created_at ASC
            """.trimIndent()
        val sqlParams =
            MapSqlParameterSource()
                .addValue("hospital_id", hospitalID.value)
        val hospitalNews =
            namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
                HospitalNewsDtoToAdmin(
                    hospitalNewsID = UUID.fromString(rs.getString("hospital_news_id")),
                    hospitalID = UUID.fromString(rs.getString("hospital_id")),
                    title = rs.getString("title"),
                    contents = rs.getString("contents"),
                    noticeImageUrl = rs.getString("notice_image_url"),
                    relatedUrl = rs.getString("related_url"),
                    postedDate = rs.getObject("posted_date", OffsetDateTime::class.java),
                )
            }
        return HospitalNewsResultToAdmin(hospitalNews)
    }
}

data class HospitalNewsDtoToAdmin(
    val hospitalNewsID: UUID,
    val hospitalID: UUID,
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

data class HospitalNewsResultToAdmin(
    val data: List<HospitalNewsDtoToAdmin>,
)
