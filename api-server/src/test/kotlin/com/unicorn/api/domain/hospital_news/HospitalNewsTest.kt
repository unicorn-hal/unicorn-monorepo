package com.unicorn.api.domain.hospital_news

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

class HospitalNewsTest {
    @Test
    fun `should create hospital news`() {
        val hospitalNews =
            HospitalNews.create(
                hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
                title = "テストタイトル",
                contents = "テストコンテンツです。テストコンテンツです。テストコンテンツです。テストコンテンツです。テストコンテンツです。",
                noticeImageUrl = "http://example.com/icon.png",
                relatedUrl = "https://example.com/",
                postedDate = LocalDateTime.of(2024, 11, 21, 12, 0, 0),
            )

        assertEquals(hospitalNews.hospitalID.value, hospitalNews.hospitalID.value)
        assertEquals("テストタイトル", hospitalNews.title.value)
        assertEquals(
            "テストコンテンツです。テストコンテンツです。テストコンテンツです。テストコンテンツです。テストコンテンツです。",
            hospitalNews.contents.value,
        )
        assertEquals("http://example.com/icon.png", hospitalNews.noticeImageUrl?.value)
        assertEquals("https://example.com/", hospitalNews.relatedUrl?.value)
        assertEquals(LocalDateTime.of(2024, 11, 21, 12, 0, 0), hospitalNews.postedDate?.value)
    }

    @Test
    fun `should create hospital news from store`() {
        val hospitalNews =
            HospitalNews.fromStore(
                hospitalNewsID = UUID.randomUUID(),
                hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
                title = "テストタイトル",
                contents = "テストコンテンツです。テストコンテンツです。テストコンテンツです。テストコンテンツです。テストコンテンツです。",
                noticeImageUrl = "http://example.com/icon.png",
                relatedUrl = "https://example.com/",
                postedDate = LocalDateTime.of(2024, 11, 21, 12, 0, 0),
            )

        assertEquals(hospitalNews.hospitalNewsID.value, hospitalNews.hospitalNewsID.value)
        assertEquals(hospitalNews.hospitalID.value, hospitalNews.hospitalID.value)
        assertEquals("テストタイトル", hospitalNews.title.value)
        assertEquals(
            "テストコンテンツです。テストコンテンツです。テストコンテンツです。テストコンテンツです。テストコンテンツです。",
            hospitalNews.contents.value,
        )
        assertEquals("http://example.com/icon.png", hospitalNews.noticeImageUrl?.value)
        assertEquals("https://example.com/", hospitalNews.relatedUrl?.value)
        assertEquals(LocalDateTime.of(2024, 11, 21, 12, 0, 0), hospitalNews.postedDate?.value)
    }

    @Test
    fun `should return an error message when null title`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                HospitalNews.create(
                    hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
                    title = "",
                    contents = "テストコンテンツです。テストコンテンツです。テストコンテンツです。テストコンテンツです。テストコンテンツです。",
                    noticeImageUrl = "http://example.com/icon.png",
                    relatedUrl = "https://example.com/",
                    postedDate = LocalDateTime.of(2024, 11, 21, 12, 0, 0),
                )
            }
        assertEquals("title should not be blank", exception.message)
    }

    @Test
    fun `should return an error message when null contents`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                HospitalNews.create(
                    hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
                    title = "テストタイトル",
                    contents = "",
                    noticeImageUrl = "http://example.com/icon.png",
                    relatedUrl = "https://example.com/",
                    postedDate = LocalDateTime.of(2024, 11, 21, 12, 0, 0),
                )
            }
        assertEquals("contents should not be blank", exception.message)
    }
}
