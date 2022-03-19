package com.ronjunevaldoz.kpaymongo

import com.ronjunevaldoz.kpaymongo.exception.PayMongoException
import com.ronjunevaldoz.kpaymongo.models.error.PayMongoErrorResponse
import com.ronjunevaldoz.kpaymongo.models.resource.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.serialization.decodeFromString


/**
 *  @see  (https://developers.paymongo.com/reference)
 */
interface IKPayMongoClient {
    // source
    suspend fun createSource(input: CreateSourceInput): SourceResponse

    /**
     *  @param [id] Source id
     */
    suspend fun getSource(id: String): SourceResponse

    // payment
    suspend fun createPayment(input: CreatePaymentInput): PaymentResponse
    // payment methods
    /**
     * A PaymentMethod resource describes which payment method was used to fulfill a payment. It is used with a PaymentIntent to collect payments.
     */
    suspend fun createPaymentMethod(input: CreatePaymentMethodInput): PaymentMethodResponse

    //     payment intents
    suspend fun createPaymentIntent(input: CreatePaymentIntentInput): PaymentIntentResponse

    /**
     *  @param [paymentIntentId] PaymentIntent id
     */
    suspend fun getPaymentIntent(paymentIntentId: String, clientKey: String? = null): PaymentIntentResponse

    /**
     *  @param [paymentIntentId] PaymentIntent id
     */
    suspend fun attachPaymentIntent(paymentIntentId: String, input: AttachPaymentIntentInput): PaymentIntentResponse

    // webhooks
    suspend fun createWebhook(input: CreateWebhookInput): WebhookResponse

    /**
     *  @param [id] Webhook id
     */
    suspend fun getWebhook(id: String): WebhookResponse
    suspend fun getWebhooks(): WebhooksResponse
}


actual class KPayMongoClient actual constructor(secretKey: String) : IKPayMongoClient {
    @OptIn(InternalAPI::class)
    private val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(AppJson)
        }
        install(Auth) {
            basic {
                sendWithoutRequest { true }
                credentials {
                    BasicAuthCredentials(username = secretKey, password = "")
                }
            }
        }
        install(ResponseObserver) {
            onResponse { response ->
            }
        }
        HttpResponseValidator {
            validateResponse { response: HttpResponse ->
                val statusCode = response.status.value
                when (statusCode) {
                    in 300..399 -> throw RedirectResponseException(response, "")
                    in 400..499 -> throw ClientRequestException(response, "")
                    in 500..599 -> throw ServerResponseException(response, "")
                }

                if (statusCode >= 600) {
                    throw ResponseException(response, "")
                }
            }

            handleResponseException { cause: Throwable ->
                val error = when (cause) {
                    is ClientRequestException -> getError(cause.response.content)
                    else -> throw cause
                }
                throw PayMongoException(error.errors)
            }
        }
    }

    private suspend fun getError(responseContent: ByteReadChannel): PayMongoErrorResponse {
        responseContent.readUTF8Line()?.let {
            return AppJson.decodeFromString(it)
        }
        throw IllegalArgumentException("not a parsable error")
    }

    override suspend fun createSource(input: CreateSourceInput): SourceResponse {
        return client.post("$baseUrl/sources") {
            contentType(ContentType.Application.Json)
            setBody(input)
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.UserAgent, "Ktor GoLearn")
            }
        }.body()
    }

    override suspend fun getSource(id: String): SourceResponse {
        return client.get("$baseUrl/sources/$id") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.UserAgent, "Ktor GoLearn")
            }
        }.body()
    }

    override suspend fun createPayment(input: CreatePaymentInput): PaymentResponse {
        return client.post("$baseUrl/payments") {
            contentType(ContentType.Application.Json)
            setBody(input)
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.UserAgent, "Ktor GoLearn")
            }
        }.body()
    }

    override suspend fun createPaymentMethod(input: CreatePaymentMethodInput): PaymentMethodResponse {
        return client.post("$baseUrl/payment_methods") {
            contentType(ContentType.Application.Json)
            setBody(input)
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.UserAgent, "Ktor GoLearn")
            }
        }.body()
    }

    override suspend fun createPaymentIntent(input: CreatePaymentIntentInput): PaymentIntentResponse {
        return client.post("$baseUrl/payment_intents") {
            contentType(ContentType.Application.Json)
            setBody(input)
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.UserAgent, "Ktor GoLearn")
            }
        }.body()
    }

    override suspend fun getPaymentIntent(paymentIntentId: String, clientKey: String?): PaymentIntentResponse {
        return client.get("$baseUrl/payment_intents/$paymentIntentId") {
            contentType(ContentType.Application.Json)
            if (clientKey != null) {
                parameter("client_key", clientKey)
            }
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.UserAgent, "Ktor GoLearn")
            }
        }.body()
    }

    override suspend fun attachPaymentIntent(paymentIntentId: String, input: AttachPaymentIntentInput): PaymentIntentResponse {
        return client.post("$baseUrl/payment_intents/$paymentIntentId/attach") {
            contentType(ContentType.Application.Json)
            setBody(input)
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.UserAgent, "Ktor GoLearn")
            }
        }.body()
    }

    override suspend fun createWebhook(input: CreateWebhookInput): WebhookResponse {
        return client.post("$baseUrl/webhooks") {
            contentType(ContentType.Application.Json)
            setBody(input)
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.UserAgent, "Ktor GoLearn")
            }
        }.body()
    }

    override suspend fun getWebhook(id: String): WebhookResponse {
        return client.get("$baseUrl/webhooks/$id") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.UserAgent, "Ktor GoLearn")
            }
        }.body()
    }

    override suspend fun getWebhooks(): WebhooksResponse {
        return client.get("$baseUrl/webhooks") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.UserAgent, "Ktor GoLearn")
            }
        }.body()
    }

    companion object {
        private const val baseUrl = "https://api.paymongo.com/v1"
    }
}