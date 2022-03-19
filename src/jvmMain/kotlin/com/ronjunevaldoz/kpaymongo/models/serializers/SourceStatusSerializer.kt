package com.ronjunevaldoz.kpaymongo.models.serializers

import com.ronjunevaldoz.kpaymongo.models.resource.Source
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class SourceStatusSerializer : KSerializer<Source.Status> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("SourceStatus", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): Source.Status {
        val string = decoder.decodeString()
        return Source.Status.values().first { it.value == string }
    }

    override fun serialize(encoder: Encoder, value: Source.Status) {
        encoder.encodeString(value.value)
    }
}