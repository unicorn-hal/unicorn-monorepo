package com.unicorn.api.application_service.account

import com.unicorn.api.domain.account.UID
import com.unicorn.api.infrastructure.account.AccountRepository
import org.springframework.stereotype.Service

interface DeleteAccountService {
    fun delete(uid: UID): Unit
}

@Service
class DeleteAccountServiceImpl(
    private val accountRepository: AccountRepository
) : DeleteAccountService {
    override fun delete(uid: UID): Unit {
        val account = accountRepository.getOrNullByUid(uid)
            ?: throw IllegalArgumentException("account not found")

        accountRepository.delete(account)
    }
}