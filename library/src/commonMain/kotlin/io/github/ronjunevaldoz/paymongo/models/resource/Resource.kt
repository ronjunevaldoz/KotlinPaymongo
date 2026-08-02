package io.github.ronjunevaldoz.paymongo.models.resource

import io.github.ronjunevaldoz.paymongo.exception.ResourceNotSupported
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Polymorphic
@Serializable(with = ResourceSerializer::class)
sealed class Resource

object ResourceSerializer : JsonContentPolymorphicSerializer<Resource>(Resource::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Resource> {
        val property = element.jsonObject
        // dispatch on the "type" field's value, not on key presence -- a resource's JSON
        // never has a key literally named "link"/"payment"/etc, only a "type" field with
        // that value
        return when (property["type"]?.jsonPrimitive?.content) {
            "link" -> Link.serializer()
            "source" -> Source.serializer()
            "payment" -> Payment.serializer()
            "webhook" -> Webhook.serializer()
            "payment_intent" -> PaymentIntent.serializer()
            "payment_method" -> PaymentMethod.serializer()
            "checkout_session" -> CheckoutSession.serializer()
            "customer" -> Customer.serializer()
            else -> throw ResourceNotSupported("Resource not yet supported. `${property["type"]}`")
        }
    }
}