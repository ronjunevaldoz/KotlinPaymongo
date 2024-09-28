package io.github.ronjunevaldoz.paymongo.models.resource

import io.github.ronjunevaldoz.paymongo.models.Address
import io.github.ronjunevaldoz.paymongo.models.Billing
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @see [data](https://developers.paymongo.com/reference/create-a-paymentmethod)
 */
@Serializable
class CreatePaymentMethodInput(
    val data: CreatePaymentMethod
) {
    companion object {
        inline fun createPaymentMethodInput(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var type: PaymentType = PaymentType.Card
        var billing: Billing? = null
        var details: CreatePaymentMethod.Details? = null
        var option: CreatePaymentMethod.Option? = null
        var metadata: Map<String, String> = emptyMap()

        fun addBilling(name: String, phone: String, email: String, address: Address) {
            billing = Billing(
                name = name,
                phone = phone,
                email = email,
                address = address
            )
        }

        fun addCard(cardNumber: String, expMonth: Int, expYear: Int, cvc: String) {
            type = PaymentType.Card
            details = CreatePaymentMethod.Details(
                cardNumber = cardNumber,
                expMonth = expMonth,
                expYear = expYear,
                cvc = cvc
            )
        }

        fun addOption(issuerId: String, tenure: Int) {
            option = CreatePaymentMethod.Option(
                card = CreatePaymentMethod.Option.Card(
                    installments = CreatePaymentMethod.Option.Card.Installments(
                        plan = CreatePaymentMethod.Option.Card.Installments.Plan(
                            issuerId = issuerId,
                            tenure = tenure
                        )
                    )
                )
            )
        }

        fun build() = CreatePaymentMethodInput(
            CreatePaymentMethod(
                attributes = CreatePaymentMethod.Attributes(this)
            )
        )
    }

    @Serializable
    data class CreatePaymentMethod(
        val attributes: Attributes
    ) {

        @Serializable
        data class Attributes(
            val type: PaymentType,
            val details: Details? = null,
            val billing: Billing? = null,
            @SerialName("payment_method_option")
            val paymentMethodOption: Option? = null,
            val metadata: Map<String, String> = emptyMap(),
        ) {
            constructor(builder: Builder) : this(
                builder.type,
                builder.details,
                builder.billing,
                builder.option,
                builder.metadata
            )
        }

        @Serializable
        data class Option(
            val card: Card
        ) {
            @Serializable
            data class Card(
                val installments: Installments
            ) {
                @Serializable
                data class Installments(
                    val plan: Plan
                ) {
                    @Serializable
                    data class Plan(
                        @SerialName("issuer_id")
                        val issuerId: String,
                        val tenure: Int
                    )
                }
            }
        }


        @Serializable
        @SerialName("PaymentMethodDetails")
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
        val type: PaymentType,
        val metadata: Map<String, String>?,
        @SerialName("created_at")
        val createdAt: Long,
        @SerialName("updated_at")
        val updatedAt: Long
    )

    @Serializable
    data class Details(
        val last4: String,
        @SerialName("exp_month")
        val expMonth: Int,
        @SerialName("exp_year")
        val expYear: Int,
    )
}