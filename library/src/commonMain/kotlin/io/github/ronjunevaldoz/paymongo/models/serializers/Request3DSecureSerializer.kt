package io.github.ronjunevaldoz.paymongo.models.serializers

import io.github.ronjunevaldoz.paymongo.models.resource.PaymentIntent
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class Request3DSecureSerializer : KSerializer<PaymentIntent.PaymentRequestOptions.Request3DSecure> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("RequestThreeDSecure", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): PaymentIntent.PaymentRequestOptions.Request3DSecure {
        val string = decoder.decodeString()
        return PaymentIntent.PaymentRequestOptions.Request3DSecure.values().find { it.value == string } ?: throw Exception("Not supported $string")
    }

    override fun serialize(encoder: Encoder, value: PaymentIntent.PaymentRequestOptions.Request3DSecure) {
        encoder.encodeString(value.value)
    }
}