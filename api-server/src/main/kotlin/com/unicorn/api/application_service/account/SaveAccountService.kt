package com.unicorn.api.application_service.account

import com.unicorn.api.domain.account.Account
import com.unicorn.api.infrastructure.account.AccountRepository
import org.springframework.stereotype.Service

interface SaveAccountService {
    fun save(
        uid: String,
        role: String,
        fcmTokenId: String
    ): Account
}

@Service
class SaveAccountServiceImpl(
    private val accountRepository: AccountRepository
): SaveAccountService {
    override fun save(
        uid: String,
        role: String,
        fcmTokenId: String
    ): Account {
        val account = Account.create(
            uid = uid,
            role = role,
            fcmTokenId = fcmTokenId
        )

        val existingAccount = accountRepository.getOrNullByUid(account.uid)

        if (existingAccount != null) {
            throw IllegalArgumentException("account already exists")
        }

        accountRepository.store(account)

        return account
    }
}