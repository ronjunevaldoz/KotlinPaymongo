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
val AppJson = Json {
    serializersModule = ResourceModule
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = true
}