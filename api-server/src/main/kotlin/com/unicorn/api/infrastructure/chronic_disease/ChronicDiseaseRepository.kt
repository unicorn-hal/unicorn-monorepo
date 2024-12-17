package com.unicorn.api.infrastructure.chronic_disease

import com.unicorn.api.domain.chronic_disease.*
import com.unicorn.api.domain.user.User
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface ChronicDiseaseRepository {
    fun store(chronicDisease: ChronicDisease): ChronicDisease

    fun getOrNullBy(chronicDiseaseID: ChronicDiseaseID): ChronicDisease?

    fun delete(chronicDisease: ChronicDisease)

    fun deleteByUser(user: User)
}

@Repository
class ChronicDiseaseRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : ChronicDiseaseRepository {
    override fun store(chronicDisease: ChronicDisease): ChronicDisease {
        // language=postgresql
        val sql =
            """
            INSERT INTO chronic_diseases (
                chronic_disease_id,
                user_id,
                disease_id,
                created_at
            ) VALUES (
                :chronicDiseaseID,
                :userID,
                :diseaseID,
                NOW()
            )
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("chronicDiseaseID", chronicDisease.chronicDiseaseID.value)
                .addValue("userID", chronicDisease.userID.value)
                .addValue("diseaseID", chronicDisease.diseaseID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
        return chronicDisease
    }

    override fun getOrNullBy(chronicDiseaseID: ChronicDiseaseID): ChronicDisease? {
        // language=postgresql
        val sql =
            """
            SELECT 
                chronic_disease_id,
                user_id,
                disease_id
            FROM chronic_diseases
            WHERE chronic_disease_id = :chronicDiseaseID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("chronicDiseaseID", chronicDiseaseID.value)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams,
        ) { rs, _ ->
            ChronicDisease.fromStore(
                chronicDiseaseID = UUID.fromString(rs.getString("chronic_disease_id")),
                userID = rs.getString("user_id"),
                diseaseID = rs.getInt("disease_id"),
            )
        }.singleOrNull()
    }

    override fun delete(chronicDisease: ChronicDisease) {
        // language=postgresql
        val sql =
            """
            UPDATE chronic_diseases
            SET deleted_at = NOW()
            WHERE chronic_disease_id = :chronicDiseaseID
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("chronicDiseaseID", chronicDisease.chronicDiseaseID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }

    override fun deleteByUser(user: User) {
        // language=postgresql
        val sql =
            """
            UPDATE chronic_diseases
            SET deleted_at = NOW()
            WHERE user_id = :userID
            AND deleted_at IS NULL;
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("userID", user.userID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}
