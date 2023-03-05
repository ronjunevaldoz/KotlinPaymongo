package com.ronjunevaldoz.kpaymongo.models.resource

import com.ronjunevaldoz.kpaymongo.models.Billing
import com.ronjunevaldoz.kpaymongo.models.serializers.PaymentStatusSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePaymentInput(
    val data: PaymentInput
) {
    @Serializable
    data class PaymentInput(
        val attributes: AttributesInput
    ) {
        @Serializable
        data class AttributesInput(
            val amount: Int,
            val description: String? = null,
            val currency: String,
            @SerialName("statement_descriptor")
            val statementDescriptor: String? = null,
            val source: SourceInput
        )

        @Serializable
        data class SourceInput(
            val id: String,
            val type: String
        )
    }
}

@Serializable
data class PaymentResponse(
    val data: Payment
)


@Serializable
data class Payment(
    val id: String,
    val attributes: Attributes
) : Resource() {
    @Serializable
    data class Attributes(
        @SerialName("access_url")
        val accessUrl: String? = null,
        val amount: Int,
        @SerialName("balance_transaction_id")
        val balanceTransactionId: String,
        val billing: Billing,
        val currency: String,
        val description: String? = null,
        val disputed: Boolean,
        @SerialName("external_reference_number")
        val externalReferenceNumber: String?=null,
        val fee: Int,
        @SerialName("foreign_fee")
        val foreignFee: Int? = null,
        @SerialName("livemode")
        val liveMode: Boolean,
        @SerialName("net_amount")
        val netAmount: Int,
        val origin: String,
        @SerialName("payment_intent_id")
        val paymentIntentId: String? = null,
        val payout: String? = null,
        val source: Source,
        @SerialName("statement_descriptor")
        val statementDescriptor: String? = null,
        val status: Status,
        @SerialName("tax_amount")
        val taxAmount: Int? = null,
        val refunds : List<String> = emptyList(),
        val taxes : List<String> = emptyList()
    )

    @Serializable
    data class Source(
        val id: String,
        val type: String,
    )

    @Serializable(with = PaymentStatusSerializer::class)
    enum class Status(val value: String) {
        // Payments
        Pending("pending"),
        Failed("failed"),
        Paid("paid")
    }
}