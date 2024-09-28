package io.github.ronjunevaldoz.paymongo.models.resource

import io.github.ronjunevaldoz.paymongo.models.Billing
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("checkout_session")
data class CheckoutSession(
    val id: String,
    val attributes: Attributes
) : Resource() {
    @Serializable
    data class Attributes(
        val billing: Billing,
        @SerialName("billing_information_fields_editable")
        val isBillingInfoFieldsEditable: String,
        @SerialName("cancel_url")
        val cancelUrl : String,
        @SerialName("checkout_url")
        val checkoutUrl : String,
        @SerialName("client_key")
        val clientKey : String,
        @SerialName("customer_email")
        val customerEmail: String? = null,
        val description: String,
        @SerialName("line_items")
        val lineItems : List<LineItem> = emptyList(),
        @SerialName("livemode")
        val liveMode : Boolean,
        val merchant: String? = null,
        val origin: String? = null,
        @SerialName("paid_at")
        val paidAt : Long? = null,
        val payments: List<Payment> = emptyList(),
        @SerialName("payment_intent")
        val paymentIntent : PaymentIntent,
        @SerialName("payment_method_types")
        val paymentMethodTypes : List<PaymentType>,
        @SerialName("payment_method_used")
        val paymentMethodUsed : PaymentType? = null,
        @SerialName("reference_number")
        val referenceNumber: String,
        @SerialName("send_email_receipt")
        val sendEmailReceipt: Boolean,
        @SerialName("show_description")
        val showDescription: Boolean,
        @SerialName("show_line_items")
        val showLineItems: Boolean,
        @SerialName("source_id")
        val sourceId : String? = null,
        val status: PaymentStatus,
        @SerialName("success_url")
        val successUrl : String,
        @SerialName("created_at")
        val createdAt : Long,
        @SerialName("updated_at")
        val updatedAt : Long,
        val metadata: Map<String, String>? = null
    )

    @Serializable
    data class LineItem(
        val amount: Int,
        val currency: String,
        val description: String,
        val images: List<String> = emptyList(),
        val name: String,
        val quantity: Int
    )
}

@Serializable
data class CreateCheckoutSessionInput(
    val data : CheckoutSessionInput
)


@Serializable
data class CheckoutSessionInput(
    val attributes : AttributesInput
) {
    @Serializable
    data class AttributesInput(
        val billing: Billing,
        val description: String,
        @SerialName("line_items")
        val lineItems: List<CheckoutSession.LineItem>,
        @SerialName("payment_method_types")
        val paymentMethodTypes: List<PaymentType>,
        @SerialName("reference_number")
        val referenceNumber: String,
        @SerialName("send_email_receipt")
        val sendEmailReceipt: Boolean = false,
        @SerialName("show_description")
        val showDescription: Boolean = true,
        @SerialName("show_line_items")
        val showLineItems: Boolean = true,
        @SerialName("cancel_url")
        val cancelUrl: String,
        @SerialName("success_url")
        val successUrl: String,
        @SerialName("statement_descriptor")
        val statementDescriptor: String,
        val metadata: Map<String, String> = emptyMap()
    )
}

@Serializable
data class CheckoutSessionResponse(
    val data: CheckoutSession
)
