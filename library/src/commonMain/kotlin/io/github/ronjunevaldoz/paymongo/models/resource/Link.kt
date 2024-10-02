package io.github.ronjunevaldoz.paymongo.models.resource

import io.github.ronjunevaldoz.paymongo.models.Tax
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateLinkInput(val data: LinkInput) {
    @Serializable
    data class LinkInput(
        val attributes: Attributes
    )

    @Serializable
    data class Attributes(
        val amount: Int,
        val description: String,
        val remarks: String
    )
}

@Serializable
@SerialName("link")
data class Link(
    val data: Data
) : Resource() {
    @Serializable
    data class Data(
        val id: String,
        val type: String,
        val attributes: Attributes
    ) {
        @Serializable
        data class Attributes(
            val amount: Int,
            val archived: Boolean,
            val currency: String,
            val description: String,
            @SerialName("livemode")
            val liveMode: Boolean,
            val fee: Int,
            val remarks: String,
            val status: String,
            @SerialName("tax_amount")
            val taxAmount: Int?,
            val taxes: List<Tax>,
            @SerialName("checkout_url")
            val checkoutUrl: String,
            @SerialName("reference_number")
            val referenceNumber: String,
            @SerialName("created_at")
            val createdAt: Long,
            @SerialName("updated_at")
            val updatedAt: Long,
            val payments: List<Payment>
        )
    }
}