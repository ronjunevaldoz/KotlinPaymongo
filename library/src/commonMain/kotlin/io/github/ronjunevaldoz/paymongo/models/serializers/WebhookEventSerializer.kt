package io.github.ronjunevaldoz.paymongo.models.serializers

import io.github.ronjunevaldoz.paymongo.exception.WebhookNotSupported
import io.github.ronjunevaldoz.paymongo.models.resource.WebhookEvent
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class WebhookEventSerializer : KSerializer<WebhookEvent.Event> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("WebhookEvent", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): WebhookEvent.Event {
        val string = decoder.decodeString()
        return WebhookEvent.Event.entries.find { it.value == string } ?: throw WebhookNotSupported("Not supported $string")
    }

    override fun serialize(encoder: Encoder, value: WebhookEvent.Event) {
        encoder.encodeString(value.value)
    }
}