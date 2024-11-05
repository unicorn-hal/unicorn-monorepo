package com.unicorn.api.infrastructure.account

import com.unicorn.api.domain.account.Account
import com.unicorn.api.domain.account.UID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

interface AccountRepository {
    fun store(account: Account): Unit

    fun getOrNullByUid(uid: UID): Account?

    fun delete(account: Account): Unit
}

@Repository
class AccountRepositoryImpl(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : AccountRepository {
    override fun store(account: Account) {
        // language=postgresql
        val sql =
            """
            INSERT INTO accounts (uid, role, fcm_token_id)
            VALUES (:uid, :role::role, :fcmTokenId)
            ON CONFLICT (uid) DO UPDATE
            SET role = EXCLUDED.role,
                fcm_token_id = EXCLUDED.fcm_token_id
            """.trimIndent()

        // language=postgresql
        val updateSql =
            """
            UPDATE accounts
            SET deleted_at = NULL
            WHERE uid = :uid
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("uid", account.uid.value)
                .addValue("role", account.role.toString())
                .addValue("fcmTokenId", account.fcmTokenId.value)

        val updateSqlParams =
            MapSqlParameterSource()
                .addValue("uid", account.uid.value)

        val deletedAccount = getDeletedAccountOrNull(account.uid)

        // 論理削除されたアカウントが存在する場合、復元する
        if (deletedAccount != null) {
            namedParameterJdbcTemplate.update(updateSql, updateSqlParams)
            return
        }

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }

    override fun getOrNullByUid(uid: UID): Account? {
        // language=postgresql
        val sql =
            """
            SELECT
                uid,
                role,
                fcm_token_id
            FROM accounts
            WHERE uid = :uid AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("uid", uid.value)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams,
        ) { rs, _ ->
            Account.fromStore(
                uid = rs.getString("uid"),
                role = rs.getString("role"),
                fcmTokenId = rs.getString("fcm_token_id"),
            )
        }.singleOrNull()
    }

    override fun delete(account: Account) {
        // language=postgresql
        val sql =
            """
            UPDATE accounts
            SET deleted_at = NOW()
            WHERE uid = :uid
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("uid", account.uid.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }

    private fun getDeletedAccountOrNull(uid: UID): Account? {
        // language=postgresql
        val sql =
            """
            SELECT
                uid,
                role,
                fcm_token_id
            FROM accounts
            WHERE uid = :uid AND deleted_at IS NOT NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("uid", uid.value)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams,
        ) { rs, _ ->
            Account.fromStore(
                uid = rs.getString("uid"),
                role = rs.getString("role"),
                fcmTokenId = rs.getString("fcm_token_id"),
            )
        }.singleOrNull()
    }
}
