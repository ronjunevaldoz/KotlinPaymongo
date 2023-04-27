package com.ronjunevaldoz.kpaymongo.models.serializers

import com.ronjunevaldoz.kpaymongo.models.resource.PaymentStatus
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class PaymentStatusSerializer : KSerializer<PaymentStatus> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PaymentStatus", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): PaymentStatus {
        val string = decoder.decodeString()
        return PaymentStatus.values().find { it.value == string } ?: throw Exception("Not supported $string")
    }

    override fun serialize(encoder: Encoder, value: PaymentStatus) {
        encoder.encodeString(value.value)
    }
}