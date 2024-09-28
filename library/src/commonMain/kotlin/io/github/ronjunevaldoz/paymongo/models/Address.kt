package io.github.ronjunevaldoz.paymongo.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val line1: String? = null,
    val line2: String? = null,
    val state: String? = null,
    @SerialName("postal_code")
    val postalCode: String? = null,
    val city: String? = null,
    val country: String? = null
)