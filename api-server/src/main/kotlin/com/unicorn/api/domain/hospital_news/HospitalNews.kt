package com.unicorn.api.domain.hospital_news

import com.unicorn.api.domain.hospital.HospitalID
import java.time.LocalDateTime
import java.util.*

data class HospitalNews(
    val hospitalNewsID: HospitalNewsID,
    val hospitalID: HospitalID,
    val title: Title,
    val contents: Contents,
    val noticeImageUrl: NoticeImageUrl?,
    val relatedUrl: RelatedUrl?,
    val postedDate: PostedDate?,
) {
    companion object {
        fun fromStore(
            hospitalNewsID: UUID,
            hospitalID: UUID,
            title: String,
            contents: String,
            noticeImageUrl: String?,
            relatedUrl: String?,
            postedDate: LocalDateTime?,
        ): HospitalNews {
            return HospitalNews(
                hospitalNewsID = HospitalNewsID(hospitalNewsID),
                hospitalID = HospitalID(hospitalID),
                title = Title(title),
                contents = Contents(contents),
                noticeImageUrl = NoticeImageUrl(noticeImageUrl),
                relatedUrl = RelatedUrl(relatedUrl),
                postedDate = PostedDate(postedDate),
            )
        }

        fun create(
            hospitalID: UUID,
            title: String,
            contents: String,
            noticeImageUrl: String?,
            relatedUrl: String?,
            postedDate: LocalDateTime?,
        ): HospitalNews {
            return HospitalNews(
                hospitalNewsID = HospitalNewsID(UUID.randomUUID()),
                hospitalID = HospitalID(hospitalID),
                title = Title(title),
                contents = Contents(contents),
                noticeImageUrl = NoticeImageUrl(noticeImageUrl),
                relatedUrl = RelatedUrl(relatedUrl),
                postedDate = PostedDate(postedDate),
            )
        }
    }
}

@JvmInline
value class HospitalNewsID(val value: UUID) {
    init {
        require(value != UUID(0L, 0L)) { "HospitalNewsID should not be null UUID" }
    }
}

@JvmInline
value class Title(val value: String) {
    init {
        require(value.isNotBlank()) { "title should not be blank" }
    }
}

@JvmInline
value class Contents(val value: String) {
    init {
        require(value.isNotBlank()) { "contents should not be blank" }
    }
}

@JvmInline
value class NoticeImageUrl(val value: String?)

@JvmInline
value class RelatedUrl(val value: String?)

@JvmInline
value class PostedDate(val value: LocalDateTime?)
