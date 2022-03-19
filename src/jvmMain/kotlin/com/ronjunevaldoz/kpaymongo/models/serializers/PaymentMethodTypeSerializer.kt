package com.ronjunevaldoz.kpaymongo.models.serializers

import com.ronjunevaldoz.kpaymongo.models.resource.PaymentMethod
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class PaymentMethodTypeSerializer : KSerializer<PaymentMethod.Type> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PaymentMethodType", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): PaymentMethod.Type {
        val string = decoder.decodeString()
        return PaymentMethod.Type.values().first { it.value == string }
    }

    override fun serialize(encoder: Encoder, value: PaymentMethod.Type) {
        encoder.encodeString(value.value)
    }
}