package com.unicorn.api.query_service.disease

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

interface DiseaseQueryService {
    fun getBy(diseaseName: String): DiseaseResult

    fun getByFamousDisease(): DiseaseResult
}

@Service
class DiseaseQueryServiceImpl(
    val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : DiseaseQueryService {
    override fun getBy(diseaseName: String): DiseaseResult {
        if (diseaseName.isEmpty()) {
            return DiseaseResult(emptyList())
        }
        // language=postgresql
        val sql =
            """
            SELECT
                diseases.disease_id,
                diseases.disease_name
            FROM diseases
            WHERE
                diseases.disease_name ILIKE '%' || :diseaseName || '%'
               OR diseases.disease_ruby ILIKE '%' || :diseaseName || '%'
            """.trimIndent()

        val params =
            MapSqlParameterSource()
                .addValue("diseaseName", diseaseName)

        val result =
            namedParameterJdbcTemplate.query(sql, params) { rs, _ ->
                Disease(
                    diseaseID = rs.getInt("disease_id"),
                    diseaseName = rs.getString("disease_name"),
                )
            }

        return DiseaseResult(result)
    }

    override fun getByFamousDisease(): DiseaseResult {
        // language=postgresql
        val sql =
            """
            SELECT
                diseases.disease_id,
                diseases.disease_name
            FROM diseases
            WHERE diseases.is_famous = true
            """.trimIndent()

        val result =
            namedParameterJdbcTemplate.query(sql, MapSqlParameterSource()) { rs, _ ->
                Disease(
                    diseaseID = rs.getInt("disease_id"),
                    diseaseName = rs.getString("disease_name"),
                )
            }

        return DiseaseResult(result)
    }
}

data class Disease(
    val diseaseID: Int,
    val diseaseName: String,
)

data class DiseaseResult(
    val data: List<Disease>,
)
