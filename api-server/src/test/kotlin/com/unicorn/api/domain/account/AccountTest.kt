package com.unicorn.api.domain.account

import org.junit.jupiter.api.Test

class AccountTest {
    @Test
    fun `should create account`() {
        val account = Account.create("uid", "user", "fcmTokenId")

        assert(account.uid.value == "uid")
        assert(account.role == Role.valueOf("user"))
        assert(account.fcmTokenId.value == "fcmTokenId")
    }

    @Test
    fun `should not create account with invalid role`() {
        try {
            Account.create("uid", "invalid", "fcmTokenId")
            assert(false)
        } catch (e: IllegalArgumentException) {
            assert(e.message == "role is invalid")
        }
    }

    @Test
    fun `should create account from store`() {
        val account = Account.fromStore("uid", "user", "fcmTokenId")

        assert(account.uid.value == "uid")
        assert(account.role == Role.valueOf("user"))
        assert(account.fcmTokenId.value == "fcmTokenId")
    }
}