package com.unicorn.api.application_service.message

import com.unicorn.api.controller.message.MessagePostRequest
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.chat.ChatID
import com.unicorn.api.domain.message.Content
import com.unicorn.api.domain.message.Message
import com.unicorn.api.domain.message.MessageSavedEvent
import com.unicorn.api.infrastructure.account.AccountRepository
import com.unicorn.api.infrastructure.chat.ChatRepository
import com.unicorn.api.infrastructure.message.MessageRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

interface SaveMessageService {
    fun save(
        chatID: ChatID,
        messagePostRequest: MessagePostRequest,
    ): MessagePostRequest
}

@Service
class SaveMessageServiceImpl(
    val accountRepository: AccountRepository,
    val chatRepository: ChatRepository,
    val messageRepository: MessageRepository,
    val eventPublisher: ApplicationEventPublisher,
) : SaveMessageService {
    override fun save(
        chatID: ChatID,
        messagePostRequest: MessagePostRequest,
    ): MessagePostRequest {
        val account = accountRepository.getOrNullByUid(UID(messagePostRequest.senderID))
        requireNotNull(account) { "Account not found" }

        val chat = chatRepository.getOrNullBy(chatID)
        requireNotNull(chat) { "Chat not found" }

        val message =
            Message.create(
                chatID = chat.chatID,
                senderID = account.uid,
                content = Content(messagePostRequest.content),
            )

        messageRepository.store(message)
        eventPublisher.publishEvent(MessageSavedEvent(message))

        return messagePostRequest
    }
}
