package io.github.ronjunevaldoz.paymongo.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline
import kotlin.math.roundToInt

/**
 * PayMongo amount fields are integers in the smallest currency unit (centavos for PHP,
 * PayMongo's only supported currency) -- e.g. PHP 100.00 is minor-unit `10000`. Wraps that
 * integer so major and minor units can't be mixed up at a call site; serializes on the wire
 * as the same plain JSON integer PayMongo always expected, unchanged.
 */
@Serializable(with = AmountSerializer::class)
@JvmInline
value class Amount(val minorUnits: Int) : Comparable<Amount> {
    val majorUnits: Double get() = minorUnits / 100.0

    operator fun plus(other: Amount): Amount = Amount(minorUnits + other.minorUnits)
    operator fun minus(other: Amount): Amount = Amount(minorUnits - other.minorUnits)
    override fun compareTo(other: Amount): Int = minorUnits.compareTo(other.minorUnits)

    companion object {
        val ZERO = Amount(0)
        fun ofMajorUnits(value: Double): Amount = Amount((value * 100).roundToInt())
    }
}

object AmountSerializer : KSerializer<Amount> {
    override val descriptor = PrimitiveSerialDescriptor("Amount", PrimitiveKind.INT)
    override fun serialize(encoder: Encoder, value: Amount) = encoder.encodeInt(value.minorUnits)
    override fun deserialize(decoder: Decoder): Amount = Amount(decoder.decodeInt())
}

/** PayMongo's only supported currency is PHP; centavos is the domestic name for [Amount.minorUnits]. */
val Amount.centavos: Int get() = minorUnits

/** `1000.centavos` builds an [Amount] directly, e.g. as a literal in a request body. */
val Int.centavos: Amount get() = Amount(this)

/** `100.0.majorUnits` builds an [Amount] from pesos, e.g. `100.0.majorUnits` == `10000.centavos`. */
val Double.majorUnits: Amount get() = Amount.ofMajorUnits(this)
