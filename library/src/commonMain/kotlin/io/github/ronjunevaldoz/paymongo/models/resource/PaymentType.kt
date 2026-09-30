package io.github.ronjunevaldoz.paymongo.models.resource

import io.github.ronjunevaldoz.paymongo.models.serializers.PaymentTypeSerializer
import kotlinx.serialization.Serializable

/**
 * Checkout sessions accept `shopee_pay`, `qrph`, `billease`, `card`, `dob`, `dob_ubp`,
 * `brankas_bdo`, `brankas_landbank`, `brankas_metrobank`, `gcash`, `grab_pay` and `paymaya`
 * ([docs](https://docs.paymongo.com/reference/create-a-checkout)). [Atome] and [Maya] are not
 * checkout session types: sending them in `payment_method_types` gets the session rejected.
 */
@Serializable(with = PaymentTypeSerializer::class)
enum class PaymentType(val value: String) {
    /** Payment intents/methods only; not accepted by checkout sessions. */
    Atome("atome"),
    Card("card"),
    Dob("dob"),
    /** Not a checkout session type; checkout sessions use [PayMaya]. */
    Maya("maya"),
    GCash("gcash"),
    GrabPay("grab_pay"),
    Billease("billease"),
    PayMaya("paymaya"),
    QrPh("qrph"),
    ShopeePay("shopee_pay"),
    DobUbp("dob_ubp"),
    BrankasBdo("brankas_bdo"),
    BrankasLandbank("brankas_landbank"),
    BrankasMetrobank("brankas_metrobank");

    companion object {
        /** The type whose wire [value] is [value], or null if this library doesn't know it. */
        fun fromValue(value: String): PaymentType? = entries.find { it.value == value }
    }
}
