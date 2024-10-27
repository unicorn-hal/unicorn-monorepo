package com.unicorn.api.domain.doctor

import com.unicorn.api.domain.department.DepartmentID
import com.unicorn.api.domain.hospital.HospitalID
import java.time.LocalTime
import java.util.*

data class Doctor private constructor (
    val doctorID: DoctorID,
    val hospitalID: HospitalID,
    val firstName: FirstName,
    val lastName: LastName,
    val email: Email,
    val phoneNumber: PhoneNumber,
    val doctorIconUrl: DoctorIconUrl?,
    val departments: List<DepartmentID>,
//    val chatSupportStartHour: ChatSupportStartHour,
//    val chatSupportEndHour: ChatSupportEndHour,
//    val callSupportStartHour: CallSupportStartHour,
//    val callSupportEndHour: CallSupportEndHour
) {
    companion object {
        fun create(
            doctorID: String,
            hospitalID: HospitalID,
            firstName: String,
            lastName: String,
            email: String,
            phoneNumber: String,
            doctorIconUrl: String?,
            departments: List<DepartmentID>,
//            chatSupportStartHour: LocalTime,
//            chatSupportEndHour: LocalTime,
//            callSupportStartHour: LocalTime,
//            callSupportEndHour: LocalTime
        ): Doctor {
            return Doctor(
                doctorID = DoctorID(doctorID),
                hospitalID = hospitalID,
                firstName = FirstName(firstName),
                lastName = LastName(lastName),
                email = Email(email),
                phoneNumber = PhoneNumber(phoneNumber),
                doctorIconUrl = doctorIconUrl?.let { DoctorIconUrl(it) },
                departments = departments,
//                chatSupportStartHour = ChatSupportStartHour(chatSupportStartHour),
//                chatSupportEndHour = ChatSupportEndHour(chatSupportEndHour),
//                callSupportStartHour = CallSupportStartHour(callSupportStartHour),
//                callSupportEndHour = CallSupportEndHour(callSupportEndHour)
            )
        }

        fun fromStore(
            doctorID: String,
            hospitalID: UUID,
            firstName: String,
            lastName: String,
            email: String,
            phoneNumber: String,
            doctorIconUrl: String?,
            departments: List<UUID>,
//            chatSupportStartHour: LocalTime,
//            chatSupportEndHour: LocalTime,
//            callSupportStartHour: LocalTime,
//            callSupportEndHour: LocalTime
        ): Doctor {
            return Doctor(
                doctorID = DoctorID(doctorID),
                hospitalID = HospitalID(hospitalID),
                firstName = FirstName(firstName),
                lastName = LastName(lastName),
                email = Email(email),
                phoneNumber = PhoneNumber(phoneNumber),
                doctorIconUrl = doctorIconUrl?.let { DoctorIconUrl(it) },
                departments = departments.map { DepartmentID(it) },
//                chatSupportStartHour = ChatSupportStartHour(chatSupportStartHour),
//                chatSupportEndHour = ChatSupportEndHour(chatSupportEndHour),
//                callSupportStartHour = CallSupportStartHour(callSupportStartHour),
//                callSupportEndHour = CallSupportEndHour(callSupportEndHour)
            )
        }
    }

    fun update(
        hospitalID: HospitalID,
        firstName: FirstName,
        lastName: LastName,
        email: Email,
        phoneNumber: PhoneNumber,
        doctorIconUrl: DoctorIconUrl?,
        departments: List<DepartmentID>,
//        chatSupportStartHour: ChatSupportStartHour,
//        chatSupportEndHour: ChatSupportEndHour,
//        callSupportStartHour: CallSupportStartHour,
//        callSupportEndHour: CallSupportEndHour
    ): Doctor {
        return this.copy(
            hospitalID = hospitalID,
            firstName = firstName,
            lastName = lastName,
            email = email,
            phoneNumber = phoneNumber,
            doctorIconUrl = doctorIconUrl,
            departments = departments,
//            chatSupportStartHour = chatSupportStartHour,
//            chatSupportEndHour = chatSupportEndHour,
//            callSupportStartHour = callSupportStartHour,
//            callSupportEndHour = callSupportEndHour
        )
    }
}

@JvmInline
value class DoctorID(val value: String) {
    init {
        require(value.isNotBlank()) { "DoctorID must not be blank" }
    }
}

@JvmInline
value class FirstName(val value: String) {
    init {
        require(value.isNotBlank()) { "first name should not be blank" }
    }
}

@JvmInline
value class LastName(val value: String) {
    init {
        require(value.isNotBlank()) { "last name should not be blank" }
    }
}

@JvmInline
value class Email(val value: String) {
    init {
        require(value.contains("@")) { "email should be valid" }
    }
}

@JvmInline
value class PhoneNumber(val value: String) {
    init {
        require(value.all { it.isDigit() }) { "phone number should be all digits" }
    }
}


@JvmInline
value class DoctorIconUrl(val value: String)

@JvmInline
value class ChatSupportStartHour(val value: LocalTime) {
    init {
        require(value.isBefore(LocalTime.of(23, 59))) { "start hour should be before 23:59" }
    }
}

@JvmInline
value class ChatSupportEndHour(val value: LocalTime) {
    init {
        require(value.isBefore(LocalTime.of(23, 59))) { "end hour should be before 23:59" }
    }
}

@JvmInline
value class CallSupportStartHour(val value: LocalTime) {
    init {
        require(value.isBefore(LocalTime.of(23, 59))) { "start hour should be before 23:59" }
    }
}

@JvmInline
value class CallSupportEndHour(val value: LocalTime) {
    init {
        require(value.isBefore(LocalTime.of(23, 59))) { "end hour should be before 23:59" }
    }
}
