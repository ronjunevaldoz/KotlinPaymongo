package com.ronjunevaldoz.kpaymongo

import com.ronjunevaldoz.kpaymongo.models.resource.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val ResourceModule = SerializersModule {
    polymorphic(Resource::class) {
        subclass(Source::class, Source.serializer())
        subclass(Payment::class, Payment.serializer())
        subclass(PaymentIntent::class, PaymentIntent.serializer())
        subclass(Webhook::class, Webhook.serializer())
    }
}
val PaymongoJson = Json {
    serializersModule = ResourceModule
    prettyPrint = true
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = true
}