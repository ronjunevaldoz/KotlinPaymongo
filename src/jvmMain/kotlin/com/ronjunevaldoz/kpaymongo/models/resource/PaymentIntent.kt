package com.ronjunevaldoz.kpaymongo.models.resource

import com.ronjunevaldoz.kpaymongo.models.serializers.PaymentIntentStatusSerializer
import com.ronjunevaldoz.kpaymongo.models.serializers.PaymentMethodAllowedSerializer
import com.ronjunevaldoz.kpaymongo.models.serializers.Request3DSecureSerializer
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
            val paymentMethodAllowed: List<PaymentIntent.PaymentMethodAllowed>,
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
data class PaymentIntent(
    val id: String,
    val attributes: Attributes
) : Resource() {
    @Serializable
    data class Attributes(
        val amount: Int,
        val currency: String,
        val description: String?,
        @SerialName("statement_descriptor")
        val statementDescriptor: String? = null,
        val status: Status,
        @SerialName("livemode")
        val liveMode: Boolean,
        @SerialName("client_key")
        val clientKey: String,
        @SerialName("last_payment_error")
        val lastPaymentError: Map<String, String>? = null,
        @SerialName("capture_type")
        val captureType: PaymentRequestOptions.Request3DSecure,
        @SerialName("payment_method_allowed")
        val paymentMethodAllowed: List<PaymentMethodAllowed>,
        val payments: List<String> = emptyList(), // array
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

    @Serializable(with = PaymentMethodAllowedSerializer::class)
    enum class PaymentMethodAllowed(val value: String) {
        Card("card"),
        PayMaya("paymaya");

        companion object {
            val All = values().toList()
        }
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

    @Serializable(with = PaymentIntentStatusSerializer::class)
    enum class Status(val value: String) {
        AwaitingPaymentMethod("awaiting_payment_method"),
        AwaitingNextAction("awaiting_next_action"),
        Processing("processing"),
        Succeed("succeeded"),
    }
}