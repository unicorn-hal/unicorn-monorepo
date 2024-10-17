package com.unicorn.api.query_service.departments

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.util.*

interface DepartmentsQueryService {
    fun get(): DepartmentsResult
}

@Service
class DepartmentsQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : DepartmentsQueryService {
    override fun get(): DepartmentsResult {
        val sql = """
            SELECT 
                "departmentID",
                "departmentName"
            FROM "Departments";
        """.trimIndent()
        val departments = namedParameterJdbcTemplate.query(
            sql,
            { rs, _ ->
                DepartmentsDto(
                    departmentID = UUID.fromString(rs.getString("departmentID")),
                    departmentName = rs.getString("departmentName")
                )
            }
        )
        return DepartmentsResult(departments)
    }
}


data class DepartmentsDto(
    val departmentID: UUID,
    val departmentName: String
)

data class DepartmentsResult(
    val data: List<DepartmentsDto>
)

