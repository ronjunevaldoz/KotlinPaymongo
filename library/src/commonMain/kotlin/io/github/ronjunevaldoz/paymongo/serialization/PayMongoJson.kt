package io.github.ronjunevaldoz.paymongo.serialization

import kotlinx.serialization.json.Json

val PayMongoJson = Json {
    serializersModule = ResourceModule
    prettyPrint = true
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = true
}