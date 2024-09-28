package io.github.ronjunevaldoz.paymongo.models.serializers

import io.github.ronjunevaldoz.paymongo.models.resource.Webhook
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class WebhookStatusSerializer : KSerializer<Webhook.Status> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("WebhookStatus", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): Webhook.Status {
        val string = decoder.decodeString()
        return Webhook.Status.values().find { it.value == string } ?: throw Exception("Not supported $string")
    }

    override fun serialize(encoder: Encoder, value: Webhook.Status) {
        encoder.encodeString(value.value)
    }
}