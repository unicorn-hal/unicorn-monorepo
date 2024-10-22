package com.unicorn.api.infrastructure.department

import com.unicorn.api.domain.department.Department
import com.unicorn.api.domain.department.DepartmentID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface DepartmentRepository {
    fun getOrNullBy(departmentID: DepartmentID): Department?
    fun findByDepartmentIDs(departmentIDs: List<DepartmentID>): List<Department?>
}

@Repository
class DepartmentRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : DepartmentRepository {
    override fun getOrNullBy(departmentID: DepartmentID): Department? {
        // language=postgresql
        val sql  = """
            SELECT department_id, department_name
            FROM departments
            WHERE department_id = :departmentID
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("departmentID", departmentID.value)

        return namedParameterJdbcTemplate.query(sql, params) { rs, _ ->
            Department.fromStore(
                departmentID = UUID.fromString(rs.getString("department_id")),
                departmentName = rs.getString("department_name")
            )
        }.singleOrNull()
    }

    override fun findByDepartmentIDs(departmentIDs: List<DepartmentID>): List<Department?> {
        // language=postgresql
        val sql = """
            SELECT department_id, department_name
            FROM departments
            WHERE department_id IN (:departmentIDs)
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("departmentIDs", departmentIDs.map { it.value })

        val result =  namedParameterJdbcTemplate.query(sql, params) { rs, _ ->
            Department.fromStore(
                departmentID = UUID.fromString(rs.getString("department_id")),
                departmentName = rs.getString("department_name")
            )
        }

        return departmentIDs.map { departmentID ->
            result.find { it.departmentID == departmentID }
        }
    }
}