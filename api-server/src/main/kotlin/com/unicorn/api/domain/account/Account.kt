package com.unicorn.api.domain.account

data class Account private constructor(
    val uid: UID,
    val role: Role,
    val fcmTokenId: FCMTokenId,
) {
    class InvalidRoleException : IllegalArgumentException("role is invalid")

    companion object {
        fun fromStore(
            uid: String,
            role: String,
            fcmTokenId: String,
        ): Account {
            return Account(
                uid = UID(uid),
                role = Role.valueOf(role),
                fcmTokenId = FCMTokenId(fcmTokenId),
            )
        }

        fun create(
            uid: String,
            role: String,
            fcmTokenId: String,
        ): Account {
            if (role !in Role.entries.map { it.name }) {
                throw InvalidRoleException()
            }

            return Account(
                uid = UID(uid),
                role = Role.valueOf(role),
                fcmTokenId = FCMTokenId(fcmTokenId),
            )
        }
    }

    fun update(fcmTokenId: FCMTokenId): Account {
        return this.copy(
            fcmTokenId = fcmTokenId,
        )
    }

    fun isUser(): Boolean {
        return role == Role.user
    }

    fun isDoctor(): Boolean {
        return role == Role.doctor
    }
}

enum class Role {
    user,
    doctor,
}

@JvmInline
value class UID(val value: String) {
    init {
        require(value.isNotBlank()) {
            "uid should not be blank"
        }
    }
}

@JvmInline
value class FCMTokenId(val value: String)
