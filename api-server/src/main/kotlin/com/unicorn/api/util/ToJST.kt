package com.unicorn.api.util

import java.time.OffsetDateTime
import java.time.ZoneId

fun OffsetDateTime.toJST(): OffsetDateTime {
    return this.atZoneSameInstant(ZoneId.of("Asia/Tokyo")).toOffsetDateTime()
}

fun OffsetDateTime.toUTC(): OffsetDateTime {
    return this.atZoneSameInstant(ZoneId.of("UTC")).toOffsetDateTime()
}
