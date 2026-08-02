package io.github.ronjunevaldoz.paymongo.models.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCustomerInput(val data: CustomerInput) {
    @Serializable
    data class CustomerInput(
        val attributes: Attributes
    )

    @Serializable
    data class Attributes(
        @SerialName("first_name")
        val firstName: String,
        @SerialName("last_name")
        val lastName: String,
        val email: String,
        @SerialName("default_device")
        val defaultDevice: String,
        val phone: String? = null
    )
}

@Serializable
data class UpdateCustomerInput(val data: CustomerInput) {
    @Serializable
    data class CustomerInput(
        val attributes: Attributes
    )

    @Serializable
    data class Attributes(
        @SerialName("first_name")
        val firstName: String? = null,
        @SerialName("last_name")
        val lastName: String? = null,
        val email: String? = null,
        @SerialName("default_device")
        val defaultDevice: String? = null,
        val phone: String? = null
    )
}

@Serializable
data class CustomerResponse(
    val data: Customer
)

@Serializable
data class CustomersResponse(
    val data: List<Customer>
)

@Serializable
data class DeletedCustomerResponse(
    val data: DeletedCustomer
)

@Serializable
data class DeletedCustomer(
    val id: String,
    val type: String,
    val attributes: Attributes
) {
    @Serializable
    data class Attributes(
        val deleted: Boolean
    )
}

@Serializable
@SerialName("customer")
data class Customer(
    val id: String,
    val attributes: Attributes
) : Resource() {
    @Serializable
    data class Attributes(
        @SerialName("first_name")
        val firstName: String,
        @SerialName("last_name")
        val lastName: String,
        val email: String,
        val phone: String? = null,
        @SerialName("default_device")
        val defaultDevice: String? = null,
        @SerialName("livemode")
        val liveMode: Boolean,
        @SerialName("created_at")
        val createdAt: Long,
        @SerialName("updated_at")
        val updatedAt: Long
    )
}
