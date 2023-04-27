package com.ronjunevaldoz.kpaymongo.models.serializers

import com.ronjunevaldoz.kpaymongo.models.resource.WebhookEvent
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
        return WebhookEvent.Event.values().find { it.value == string } ?: throw Exception("Not supported $string")
    }

    override fun serialize(encoder: Encoder, value: WebhookEvent.Event) {
        encoder.encodeString(value.value)
    }
}