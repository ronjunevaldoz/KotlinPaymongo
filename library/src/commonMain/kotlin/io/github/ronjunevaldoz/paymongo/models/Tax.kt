package io.github.ronjunevaldoz.paymongo.models

import kotlinx.serialization.Serializable

@Serializable
data class Tax(
    val amount: Int,
    val currency: String,
    val inclusive: Boolean,
    val name: String,
    val type: String,
    val value: String
)