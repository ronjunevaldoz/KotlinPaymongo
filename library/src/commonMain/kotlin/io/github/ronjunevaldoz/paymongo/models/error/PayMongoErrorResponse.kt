package io.github.ronjunevaldoz.paymongo.models.error

import kotlinx.serialization.Serializable

@Serializable
data class PayMongoErrorResponse(
    val errors: List<PayMongoError>
)

@Serializable
data class PayMongoError(
    val code: String,   //if by errorCode you mean the http status code is not really necessary to include here as you already know it from the validateResponse
    val detail: String,
    val source: Source? = null
) {
    @Serializable
    data class Source(
        val pointer: String,
        val attribute: String
    )
}