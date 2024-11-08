package com.unicorn.api.domain.message

import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.chat.ChatID
import com.unicorn.api.util.toUTC
import java.time.OffsetDateTime
import java.util.UUID

data class Message private constructor(
    val messageID: MessageID,
    val chatID: ChatID,
    val senderID: UID,
    val sentAt: SentAt,
    val content: Content,
) {
    companion object {
        fun create(
            chatID: ChatID,
            senderID: UID,
            content: Content,
        ): Message {
            return Message(
                messageID = MessageID(UUID.randomUUID()),
                senderID = senderID,
                chatID = chatID,
                sentAt = SentAt(OffsetDateTime.now().toUTC()),
                content = content,
            )
        }

        fun fromStore(
            messageID: UUID,
            chatID: UUID,
            senderID: String,
            sentAt: OffsetDateTime,
            content: String,
        ): Message {
            return Message(
                messageID = MessageID(messageID),
                chatID = ChatID(chatID),
                senderID = UID(senderID),
                sentAt = SentAt(sentAt),
                content = Content(content),
            )
        }
    }

    fun isSender(senderID: UID): Boolean {
        return this.senderID == senderID
    }

    // TODO(メッセージ内容の更新処理は、仕様未定義につき未実装)
}

@JvmInline
value class MessageID(val value: UUID)

@JvmInline
value class Content(val value: String) {
    init {
        require(value.isNotBlank()) { "Content must not be blank" }
    }
}

// データ取得を行う際は、JSTに変換して返す
@JvmInline
value class SentAt(val value: OffsetDateTime)
