package com.unicorn.api.application_service.message

import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.chat.ChatID
import com.unicorn.api.domain.message.MessageID
import com.unicorn.api.infrastructure.account.AccountRepository
import com.unicorn.api.infrastructure.chat.ChatRepository
import com.unicorn.api.infrastructure.message.MessageRepository
import org.springframework.stereotype.Service

interface DeleteMessageService {
    fun delete(
        uid: UID,
        chatID: ChatID,
        messageID: MessageID,
    )
}

@Service
class DeleteMessageServiceImpl(
    private val accountRepository: AccountRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
) : DeleteMessageService {
    override fun delete(
        uid: UID,
        chatID: ChatID,
        messageID: MessageID,
    ) {
        val account = accountRepository.getOrNullByUid(uid)
        requireNotNull(account) { "Account not found" }

        val chat = chatRepository.getOrNullBy(chatID)
        requireNotNull(chat) { "Chat not found" }

        val message = messageRepository.getOrNullBy(messageID)
        requireNotNull(message) { "Message not found" }
        require(message.isSender(account.uid)) { "You are not the sender of this message" }

        messageRepository.delete(message)
    }
}
