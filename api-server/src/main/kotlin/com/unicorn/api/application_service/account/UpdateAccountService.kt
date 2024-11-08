package com.unicorn.api.application_service.account

import com.unicorn.api.controller.account.AccountPutRequest
import com.unicorn.api.domain.account.FCMTokenId
import com.unicorn.api.domain.account.UID
import com.unicorn.api.infrastructure.account.AccountRepository
import org.springframework.stereotype.Service

interface UpdateAccountService {
    fun update(
        uid: UID,
        accountPutRequest: AccountPutRequest,
    ): AccountPutRequest
}

@Service
class UpdateAccountServiceImpl(
    val accountRepository: AccountRepository,
) : UpdateAccountService {
    override fun update(
        uid: UID,
        accountPutRequest: AccountPutRequest,
    ): AccountPutRequest {
        val account = accountRepository.getOrNullByUid(uid)
        requireNotNull(account) { "account not found" }

        val updatedAccount = account.update(FCMTokenId(accountPutRequest.fcmTokenId))

        accountRepository.store(updatedAccount)

        return accountPutRequest
    }
}
