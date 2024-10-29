package com.unicorn.api.domain.department

import org.junit.jupiter.api.Test
import java.util.UUID

class DepartmentTest {

    @Test
    fun `should create department`() {
        val departmentID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
        val departmentName = "Cardiology"

        val department = Department.fromStore(departmentID, departmentName)

        assert(department.departmentID.value == departmentID)
        assert(department.departmentName.value == departmentName)
    }
}