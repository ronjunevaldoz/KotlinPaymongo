package com.ronjunevaldoz.kpaymongo.models.serializers

import com.ronjunevaldoz.kpaymongo.models.resource.Payment
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class PaymentStatusSerializer : KSerializer<Payment.Status> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PaymentStatus", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): Payment.Status {
        val string = decoder.decodeString()
        return Payment.Status.values().first { it.value == string }
    }

    override fun serialize(encoder: Encoder, value: Payment.Status) {
        encoder.encodeString(value.value)
    }
}