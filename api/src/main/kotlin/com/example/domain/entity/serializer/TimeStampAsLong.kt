package com.example.domain.entity.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.sql.Timestamp

object TimeStampAsLong: KSerializer<Timestamp> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date:", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Timestamp {
        return Timestamp(decoder.decodeLong())
    }

    override fun serialize(encoder: Encoder, value: Timestamp) {
        encoder.encodeLong(value.time)
    }

}