package com.ronjunevaldoz.kpaymongo

import io.ktor.client.engine.*
import kotlinx.serialization.json.Json


expect class Paymongo(config: Config = Config()) {
    class Config(
        secretKey: String = "",
        userAgent: String = "Ktor Paymongo",
        engine: HttpClientEngine? = null,
        json: Json? = null
    )
}