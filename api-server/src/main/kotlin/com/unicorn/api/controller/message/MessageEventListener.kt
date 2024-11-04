package com.unicorn.api.controller.message

import com.unicorn.api.domain.message.MessageSavedEvent
import com.unicorn.api.query_service.message.MessageQueryService
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

interface MessageEventListener {
    fun onMessageSaved(messageSavedEvent: MessageSavedEvent)
}

@Component
class MessageEventListenerImpl(
    private val messageQueryService: MessageQueryService,
    private val simpMessagingTemplate: SimpMessagingTemplate,
) : MessageEventListener {
    @EventListener
    override fun onMessageSaved(messageSavedEvent: MessageSavedEvent) {
        val message = messageQueryService.getOrNullBy(messageSavedEvent.message.messageID) ?: return
        simpMessagingTemplate.convertAndSend("/topic/chats/messages", message)
    }
}
