package com.unicorn.api.infrastructure.hospital_news

import com.unicorn.api.domain.hospital_news.HospitalNews
import com.unicorn.api.domain.hospital_news.HospitalNewsID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

interface HospitalNewsRepository {
    fun store(hospitalNews: HospitalNews): HospitalNews

    fun getOrNullBy(hospitalNewsID: HospitalNewsID): HospitalNews?

    fun delete(hospitalNews: HospitalNews): Unit
}

@Repository
class HospitalNewsImpl(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : HospitalNewsRepository {
    override fun store(hospitalNews: HospitalNews): HospitalNews {
        // language=postgresql
        val sql =
            """
            INSERT INTO hospital_news (
                "hospital_news_id",
                "hospital_id",
                "title",
                "contents",
                "notice_image_url",
                "related_url",
                "posted_date",
                "created_at",
                "deleted_at"
            ) VALUES (
                :hospitalNewsID,
                :hospitalID,
                :title,
                :contents,
                :noticeImageUrl,
                :relatedUrl,
                :postedDate,
                NOW(),
                NULL
            );
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("hospitalNewsID", hospitalNews.hospitalNewsID.value)
                .addValue("hospitalID", hospitalNews.hospitalID.value)
                .addValue("title", hospitalNews.title.value)
                .addValue("contents", hospitalNews.contents.value)
                .addValue("noticeImageUrl", hospitalNews.noticeImageUrl?.value)
                .addValue("relatedUrl", hospitalNews.relatedUrl?.value)
                .addValue("postedDate", hospitalNews.postedDate?.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
        return hospitalNews
    }

    override fun getOrNullBy(hospitalNewsID: HospitalNewsID): HospitalNews? {
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
            HospitalNews.fromStore(
                hospitalNewsID = UUID.fromString(rs.getString("hospital_news_id")),
                hospitalID = UUID.fromString(rs.getString("hospital_id")),
                title = rs.getString("title"),
                contents = rs.getString("contents"),
                noticeImageUrl = rs.getString("notice_image_url"),
                relatedUrl = rs.getString("related_url"),
                postedDate = rs.getObject("posted_date", LocalDateTime::class.java),
            )
        }.singleOrNull()
    }

    override fun delete(hospitalNews: HospitalNews) {
        // language=postgresql
        val sql =
            """
            UPDATE hospital_news
            SET deleted_at = NOW()
            WHERE hospital_news_id = :hospitalNewsID
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("hospitalNewsID", hospitalNews.hospitalNewsID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}
