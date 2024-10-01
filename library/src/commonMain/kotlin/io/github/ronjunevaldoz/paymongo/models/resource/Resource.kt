package io.github.ronjunevaldoz.paymongo.models.resource

import io.github.ronjunevaldoz.paymongo.exception.ResourceNotSupported
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Polymorphic
@Serializable(with = ResourceSerializer::class)
sealed class Resource

object ResourceSerializer : JsonContentPolymorphicSerializer<Resource>(Resource::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Resource> {
        val property = element.jsonObject
        return when {
            "link" in property -> Link.serializer()
            "source" in property -> Source.serializer()
            "payment" in property -> Payment.serializer()
            "webhook" in property -> Webhook.serializer()
            "payment_intent" in property -> PaymentIntent.serializer()
            "payment_method" in property -> PaymentMethod.serializer()
            "checkout_session" in property -> CheckoutSession.serializer()
            else -> throw ResourceNotSupported("Resource not yet supported. `${property["type"]}`")
        }
    }
}