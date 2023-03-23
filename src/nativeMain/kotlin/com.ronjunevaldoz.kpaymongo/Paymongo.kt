package com.ronjunevaldoz.kpaymongo

import io.ktor.client.engine.*
import kotlinx.serialization.json.Json


actual class Paymongo actual constructor(config: Config) {
    actual class Config actual constructor(
        var secretKey: String,
        var userAgent: String,
        var engine: HttpClientEngine?,
        var json: Json?
    )
}