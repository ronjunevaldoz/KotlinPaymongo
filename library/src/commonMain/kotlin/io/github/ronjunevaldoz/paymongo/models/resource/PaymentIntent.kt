package io.github.ronjunevaldoz.paymongo.models.resource

import io.github.ronjunevaldoz.paymongo.models.serializers.Request3DSecureSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePaymentIntentInput(
    val data: PaymentIntentInput
) {
    @Serializable
    data class PaymentIntentInput(
        val attributes: AttributesInput
    ) {
        @Serializable
        data class AttributesInput(
            val amount: Int,
            @SerialName("payment_method_allowed")
            val paymentMethodAllowed: List<PaymentType>,
            @SerialName("payment_method_options")
            val paymentMethodOptions: PaymentIntent.PaymentRequestOptions? = null,
            val description: String? = null,
            val statementDescriptor: String? = null,
            val currency: String,
            val metadata: Map<String, String> = emptyMap()
        )
    }
}

@Serializable
data class AttachPaymentIntentInput(
    val data: PaymentIntentAttach
) {
    @Serializable
    data class Attributes(
        @SerialName("payment_method")
        val paymentMethodId: String,
        @SerialName("client_key")
        val clientKey: String? = null,
        @SerialName("return_url")
        val returnUrl: String?
    )
    @Serializable
    data class PaymentIntentAttach(
       val attributes: Attributes
    )
}

@Serializable
data class PaymentIntentResponse(
    val data: PaymentIntent
)

@Serializable
@SerialName("payment_intent")
data class PaymentIntent(
    val id: String,
    val attributes: Attributes
) : Resource() {
    @Serializable
    data class Attributes(
        val amount: Int,
        val currency: String,
        val description: String? = null,
        @SerialName("statement_descriptor")
        val statementDescriptor: String? = null,
        val status: PaymentStatus,
        @SerialName("livemode")
        val liveMode: Boolean,
        @SerialName("client_key")
        val clientKey: String,
        @SerialName("last_payment_error")
        val lastPaymentError: Map<String, String>? = null,
        @SerialName("capture_type")
        val captureType: PaymentRequestOptions.Request3DSecure,
        @SerialName("payment_method_allowed")
        val paymentMethodAllowed: List<PaymentType>,
        val payments: List<Payment> = emptyList(), // array
        @SerialName("next_action")
        val nextAction: NextAction?, // array
        @SerialName("payment_method_options")
        val paymentMethodOptions: PaymentRequestOptions,
        val metadata: Map<String, String>?,
        @SerialName("created_at")
        val createdAt: Long,
        @SerialName("updated_at")
        val updatedAt: Long,
    )

    @Serializable
    data class NextAction(
        val type: String,
        val redirect: Redirect
    ) {
        @Serializable
        data class Redirect(
            val url: String,
            @SerialName("return_url")
            val returnUrl: String
        )
    }

    @Serializable
    data class PaymentRequestOptions(
        val card: Card
    ) {
        @Serializable
        data class Card(
            @SerialName("request_three_d_secure")
            val requestThreeDSecure: Request3DSecure
        )

        @Serializable(with = Request3DSecureSerializer::class)
        enum class Request3DSecure(val value: String) {
            Any("any"),
            Automatic("automatic")
        }

    }

}