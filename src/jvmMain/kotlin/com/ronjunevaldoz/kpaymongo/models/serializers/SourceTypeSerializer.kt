package com.ronjunevaldoz.kpaymongo.models.serializers

import com.ronjunevaldoz.kpaymongo.models.resource.Source
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class SourceTypeSerializer : KSerializer<Source.Type> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("SourceType", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): Source.Type {
        val string = decoder.decodeString()
        return Source.Type.values().first { it.value == string }
    }

    override fun serialize(encoder: Encoder, value: Source.Type) {
        encoder.encodeString(value.value)
    }
}