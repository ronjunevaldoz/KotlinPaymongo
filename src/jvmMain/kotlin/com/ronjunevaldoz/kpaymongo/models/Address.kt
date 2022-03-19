package com.ronjunevaldoz.kpaymongo.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val line1: String,
    val line2: String,
    val state: String,
    @SerialName("postal_code")
    val postalCode: String,
    val city: String,
    val country: String
)