package com.unicorn.api.query_service.chronic_disease

import com.unicorn.api.domain.user.UserID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.util.*

interface ChronicDiseaseQueryService {
    fun get(userID: UserID): ChronicDiseaseResult
}

@Service
class ChronicDiseaseQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : ChronicDiseaseQueryService {
    override fun get(userID: UserID): ChronicDiseaseResult {
        //language=postgresql
        val sql =
            """
            SELECT
                table1.chronic_disease_id,
                table2.disease_name
            FROM
                chronic_diseases AS table1
            INNER JOIN
                diseases AS table2
            ON
                table1.disease_id = table2.disease_id
            WHERE
                table1.user_id = :userID
            AND
                table1.deleted_at IS NULL
            """.trimIndent()
        val params = MapSqlParameterSource().addValue("userID", userID.value)
        val chronicDiseases =
            namedParameterJdbcTemplate.query(sql, params) { rs, _ ->
                ChronicDiseaseDto(
                    chronicDiseaseID = UUID.fromString(rs.getString("chronic_disease_id")),
                    diseaseName = rs.getString("disease_name"),
                )
            }
        return ChronicDiseaseResult(chronicDiseases)
    }
}

data class ChronicDiseaseDto(
    val chronicDiseaseID: UUID,
    val diseaseName: String,
)

data class ChronicDiseaseResult(
    val data: List<ChronicDiseaseDto>,
)
