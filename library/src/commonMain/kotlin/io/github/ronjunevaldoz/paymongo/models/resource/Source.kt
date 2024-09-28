package io.github.ronjunevaldoz.paymongo.models.resource

import io.github.ronjunevaldoz.paymongo.models.Billing
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSourceInput(
    val data: SourceInput
) {

    @Serializable
    data class SourceInput(
        val attributes: AttributesInput
    )

    @Serializable
    data class AttributesInput(
        val type: PaymentType,
        val amount: Int,
        val currency: String,
        val redirect: RedirectInput,
        val billing: Billing? = null
    )

    @Serializable
    data class RedirectInput(
        val success: String,
        val failed: String,
    )
}

@Serializable
data class SourceResponse(
    val data: Source
)

@Serializable
@SerialName("source")
data class Source(
    val id: String,
    val attributes: Attributes
) : Resource() {
    @Serializable
    data class Attributes(
        val amount: Int,
        val billing: Billing? = null,
        val currency: String,
        val description: String? = null,
        @SerialName("livemode")
        val liveMode: Boolean = false,
        val redirect: Redirect,
        @SerialName("statement_descriptor")
        val statementDescriptor: String? = null,
        val status: PaymentStatus,
        val type: PaymentType,
        val metadata: Map<String, String>? = null,
        @SerialName("created_at")
        val createdAt: Long,
        @SerialName("updated_at")
        val updatedAt: Long? = null
    )

    @Serializable
    data class Redirect(
        @SerialName("checkout_url")
        val checkoutUrl: String,
        val success: String,
        val failed: String,
    )
}