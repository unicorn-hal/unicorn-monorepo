package com.unicorn.api.domain.department

import java.util.*

data class Department(
    val departmentID: DepartmentID,
    val departmentName: DepartmentName,
) {
    companion object {
        fun fromStore(
            departmentID: UUID,
            departmentName: String,
        ): Department {
            return Department(
                departmentID = DepartmentID(departmentID),
                departmentName = DepartmentName(departmentName),
            )
        }
    }
}

@JvmInline
value class DepartmentID(val value: UUID)

@JvmInline
value class DepartmentName(val value: String)
