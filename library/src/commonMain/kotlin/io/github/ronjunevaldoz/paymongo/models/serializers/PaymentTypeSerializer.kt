package io.github.ronjunevaldoz.paymongo.models.serializers

import io.github.ronjunevaldoz.paymongo.models.resource.PaymentType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class PaymentTypeSerializer : KSerializer<PaymentType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Type", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): PaymentType {
        val string = decoder.decodeString()
        return PaymentType.values().find { it.value == string } ?: throw Exception("Type not yet supported $string")
    }

    override fun serialize(encoder: Encoder, value: PaymentType) {
        encoder.encodeString(value.value)
    }
}