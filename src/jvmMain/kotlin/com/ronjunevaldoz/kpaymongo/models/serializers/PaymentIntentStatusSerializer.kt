package com.ronjunevaldoz.kpaymongo.models.serializers

import com.ronjunevaldoz.kpaymongo.models.resource.PaymentIntent
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class PaymentIntentStatusSerializer : KSerializer<PaymentIntent.Status> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PaymentIntentStatus", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): PaymentIntent.Status {
        val string = decoder.decodeString()
        return PaymentIntent.Status.values().first { it.value == string }
    }

    override fun serialize(encoder: Encoder, value: PaymentIntent.Status) {
        encoder.encodeString(value.value)
    }
}