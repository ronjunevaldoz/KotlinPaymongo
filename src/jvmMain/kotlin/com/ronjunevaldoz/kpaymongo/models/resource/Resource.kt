package com.ronjunevaldoz.kpaymongo.models.resource

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Polymorphic
@Serializable(with = ResourceSerializer::class)
sealed class Resource

object ResourceSerializer : JsonContentPolymorphicSerializer<Resource>(Resource::class) {
    override fun selectDeserializer(element: JsonElement) = when {
        "source" in element.jsonObject -> Source.serializer()
        "payment" in element.jsonObject -> Payment.serializer()
        "webhook" in element.jsonObject -> Webhook.serializer()
        "payment_intent" in element.jsonObject -> PaymentIntent.serializer()
        "payment_method" in element.jsonObject -> PaymentMethod.serializer()
        else -> throw Exception("Resource not yet supported. `${element.jsonObject["type"]}`")
    }
}