package com.unicorn.api.domain.greeting

import java.util.*

data class Greeting(
    val id: UUID,
    val message: String,
) {
    companion object {
        fun fromStore(
            id: UUID,
            message: String,
        ): Greeting {
            return Greeting(
                id,
                message,
            )
        }

        fun create(message: String): Greeting {
            check(message.isNotEmpty()) { "message is empty" }
            return Greeting(
                UUID.randomUUID(),
                message,
            )
        }
    }

    fun changeMessage(message: String): Greeting {
        check(message.isNotEmpty()) { "message is empty" }
        return Greeting(
            this.id,
            message,
        )
    }
}
