package com.unicorn.api.infrastructure.account

import com.unicorn.api.domain.account.Account
import com.unicorn.api.domain.account.UID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@Transactional
@Sql("/db/account/Insert_Account_Data.sql")
@Sql("/db/account/Insert_Deleted_Account_Data.sql")
class AccountRepositoryTest {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findAccountByUid(uid: String): Account? {
        // language=postgresql
        val sql =
            """
            SELECT uid, role, fcm_token_id FROM accounts WHERE uid = :uid AND deleted_at IS NULL
            """.trimIndent()
        val sqlParams = MapSqlParameterSource().addValue("uid", uid)
        val result =
            namedParameterJdbcTemplate.query(
                sql,
                sqlParams,
            ) { rs, _ ->
                Account.fromStore(
                    uid = rs.getString("uid"),
                    role = rs.getString("role"),
                    fcmTokenId = rs.getString("fcm_token_id"),
                )
            }.firstOrNull()
        return result
    }

    @Test
    fun `should store account`() {
        val account =
            Account.create(
                uid = "uid",
                role = "user",
                fcmTokenId = "fcmTokenId",
            )

        accountRepository.store(account)

        val storedAccount = findAccountByUid(account.uid.value)

        assert(storedAccount?.uid == account.uid)
        assert(storedAccount?.role == account.role)
        assert(storedAccount?.fcmTokenId == account.fcmTokenId)
    }

    @Test
    fun `should store account with already deleted uid`() {
        val account =
            Account.create(
                uid = "test2",
                role = "user",
                fcmTokenId = "fcm_token_id",
            )

        accountRepository.store(account)

        val storedAccount = findAccountByUid(account.uid.value)

        assert(storedAccount?.uid == account.uid)
        assert(storedAccount?.role == account.role)
        assert(storedAccount?.fcmTokenId == account.fcmTokenId)
    }

    @Test
    fun `should find account by uid`() {
        val account =
            Account.create(
                uid = "test",
                role = "user",
                fcmTokenId = "fcm_token_id",
            )

        val foundAccount = accountRepository.getOrNullByUid(account.uid)

        assert(foundAccount?.uid == account.uid)
        assert(foundAccount?.role == account.role)
        assert(foundAccount?.fcmTokenId == account.fcmTokenId)
    }

    @Test
    fun `should not find account by uid`() {
        val account = accountRepository.getOrNullByUid(UID("non_existent_uid"))

        assert(account == null)
    }

    @Test
    fun `should delete account`() {
        val account =
            Account.create(
                uid = "test",
                role = "user",
                fcmTokenId = "fcm_token_id",
            )

        accountRepository.delete(account)

        val storedAccount = findAccountByUid(account.uid.value)

        assert(storedAccount == null)
    }

    @Test
    fun `should update account`() {
        val account =
            Account.create(
                uid = "test",
                role = "user",
                fcmTokenId = "new_fcm_token_id",
            )

        accountRepository.store(account)

        val storedAccount = findAccountByUid(account.uid.value)
        assert(storedAccount?.uid == account.uid)
        assert(storedAccount?.role == account.role)
        assert(storedAccount?.fcmTokenId == account.fcmTokenId)
    }
}
