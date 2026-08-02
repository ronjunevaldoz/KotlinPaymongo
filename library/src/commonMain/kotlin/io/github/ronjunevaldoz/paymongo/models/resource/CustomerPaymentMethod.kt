package io.github.ronjunevaldoz.paymongo.models.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * PayMongo has no standalone `/payment_methods/{id}` retrieve/update endpoint -- only
 * customer-scoped payment methods exist. This targets the v2 API
 * (`/v2/customer_payment_methods`), which PayMongo's docs recommend over v1.
 * @see (https://docs.paymongo.com/reference/get_customer_payment_methods)
 */
@Serializable
data class CustomerPaymentMethodsResponse(
    val data: CustomerPaymentMethodsData,
    @SerialName("has_more")
    val hasMore: Boolean = false
)

@Serializable
data class CustomerPaymentMethodsData(
    @SerialName("customer_payment_methods")
    val customerPaymentMethods: List<CustomerPaymentMethod>,
    @SerialName("last_evaluated_payment_method_id")
    val lastEvaluatedPaymentMethodId: String? = null
)

@Serializable
data class CustomerPaymentMethod(
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("customer_id")
    val customerId: String,
    @SerialName("live_mode")
    val liveMode: Boolean,
    @SerialName("payment_method_id")
    val paymentMethodId: String,
    @SerialName("session_type")
    val sessionType: String,
    @SerialName("source_type")
    val sourceType: String,
    @SerialName("updated_at")
    val updatedAt: Long
)

@Serializable
data class DeletedCustomerPaymentMethodResponse(
    val data: DeletedCustomerPaymentMethod,
    @SerialName("has_more")
    val hasMore: Boolean = false
)

@Serializable
data class DeletedCustomerPaymentMethod(
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("customer_id")
    val customerId: String,
    @SerialName("deleted_at")
    val deletedAt: Long,
    @SerialName("live_mode")
    val liveMode: Boolean,
    @SerialName("payment_method_id")
    val paymentMethodId: String,
    @SerialName("session_type")
    val sessionType: String,
    @SerialName("source_type")
    val sourceType: String,
    @SerialName("updated_at")
    val updatedAt: Long
)
