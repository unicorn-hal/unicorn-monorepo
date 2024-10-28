package com.unicorn.api.domain.app_config

data class AppConfig private constructor(
    val available: Boolean
) {
    companion object {
        fun fromStore(available: Boolean): AppConfig {
            return AppConfig(available)
        }

        fun create(available: Boolean): AppConfig {
            return AppConfig(available)
        }
    }
}
