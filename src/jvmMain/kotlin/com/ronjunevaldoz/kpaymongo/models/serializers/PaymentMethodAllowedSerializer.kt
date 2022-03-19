package com.ronjunevaldoz.kpaymongo.models.serializers

import com.ronjunevaldoz.kpaymongo.models.resource.PaymentIntent
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class PaymentMethodAllowedSerializer : KSerializer<PaymentIntent.PaymentMethodAllowed> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("PaymentMethodAllowed", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): PaymentIntent.PaymentMethodAllowed {
        val string = decoder.decodeString()
        return PaymentIntent.PaymentMethodAllowed.values().first { it.value == string }
    }

    override fun serialize(encoder: Encoder, value: PaymentIntent.PaymentMethodAllowed) {
        encoder.encodeString(value.value)
    }
}