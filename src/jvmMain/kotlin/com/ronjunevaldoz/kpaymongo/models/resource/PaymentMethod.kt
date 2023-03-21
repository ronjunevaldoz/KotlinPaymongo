package com.ronjunevaldoz.kpaymongo.models.resource

import com.ronjunevaldoz.kpaymongo.models.Billing
import com.ronjunevaldoz.kpaymongo.models.serializers.PaymentMethodTypeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePaymentMethodInput(
    val data: CreatePaymentMethod
) {

    @Serializable
    data class CreatePaymentMethod(
        val attributes: Attributes
    ) : Resource() {

        @Serializable
        data class Attributes(
            val type: PaymentMethod.Type,
            val details: Details?,
            val billing: Billing,
            val metadata: Map<String, String> = emptyMap(),
        )

        @Serializable
        data class Details(
            @SerialName("card_number")
            val cardNumber: String,
            @SerialName("exp_month")
            val expMonth: Int,
            @SerialName("exp_year")
            val expYear: Int,
            val cvc: String
        )
    }
}

@Serializable
data class PaymentMethodResponse(
    val data: PaymentMethod
)

@Serializable
@SerialName("payment_method")
data class PaymentMethod(
    val id: String,
    val attributes: Attributes
) : Resource() {

    @Serializable
    data class Attributes(
        val billing: Billing,
        val details: Details,
        @SerialName("livemode")
        val liveMode: Boolean,
        val type: Type,
        val metadata: Map<String, String>?,
        @SerialName("created_at")
        val createdAt: Long,
        @SerialName("updated_at")
        val updatedAt: Long
    )

    @Serializable(with = PaymentMethodTypeSerializer::class)
    enum class Type(val value: String) {
        Card("card"),
        PayMaya("paymaya")
    }

    @Serializable
    data class Details(
        val last4: String,
        @SerialName("exp_month")
        val expMonth: Int,
        @SerialName("exp_year")
        val expYear: Int,
    )
}