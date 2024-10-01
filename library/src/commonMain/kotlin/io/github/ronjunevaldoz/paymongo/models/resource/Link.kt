package io.github.ronjunevaldoz.paymongo.models.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateLinkInput(val data: Attributes) {
    @Serializable
    data class Attributes(
        val amount: Int,
        val description: String,
        val remarks: String
    )
}

/**
 * {
 *   "data": {
 *     "id": "link_WrnsXCjNtdv8wfRDwcs6APjy",
 *     "type": "link",
 *     "attributes": {
 *       "amount": 10000,
 *       "archived": false,
 *       "currency": "PHP",
 *       "description": "test payment for subscription",
 *       "livemode": false,
 *       "fee": 0,
 *       "remarks": "internal use descirption",
 *       "status": "unpaid",
 *       "tax_amount": null,
 *       "taxes": [],
 *       "checkout_url": "https://pm.link/org-GapS1xaVTL395KW4ucNFgpkw/test/NJUgWgz",
 *       "reference_number": "NJUgWgz",
 *       "created_at": 1727713221,
 *       "updated_at": 1727713221,
 *       "payments": []
 *     }
 *   }
 * }
 */
@Serializable
data class PaymentLink(
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
            val taxes: List<String>,
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