package com.ronjunevaldoz.kpaymongo.models
import kotlinx.serialization.Serializable

@Serializable
data class Billing(
    val name: String,
    val phone: String,
    val email: String,
    val address: Address
)