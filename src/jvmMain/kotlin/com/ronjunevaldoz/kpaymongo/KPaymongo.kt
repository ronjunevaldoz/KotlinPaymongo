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
import io.ktor.client.plugins.contentnegotiation.*
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
     *  @param [webhookId] Webhook id
     */
    suspend fun getWebhook(webhookId: String): WebhookResponse
    suspend fun getWebhooks(): WebhooksResponse
    suspend fun disableWebhook(webhookId: String): WebhookResponse
    suspend fun enabledWebhook(webhookId: String): WebhookResponse
    suspend fun updateWebhook(webhookId: String, input: CreateWebhookInput): WebhookResponse
}


actual class KPayMongoClient actual constructor(secretKey: String) : IKPayMongoClient {
    @OptIn(InternalAPI::class)
    private val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(UserAgent) {
            agent = "ktor KotlinPaymongo"
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
        defaultRequest {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Accept, "application/json")
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
            setBody(input)
        }.body()
    }

    override suspend fun getSource(id: String): SourceResponse {
        return client.get("$baseUrl/sources/$id").body()
    }

    override suspend fun createPayment(input: CreatePaymentInput): PaymentResponse {
        return client.post("$baseUrl/payments") {
            setBody(input)
        }.body()
    }

    override suspend fun createPaymentMethod(input: CreatePaymentMethodInput): PaymentMethodResponse {
        return client.post("$baseUrl/payment_methods") {
            setBody(input)
        }.body()
    }

    override suspend fun createPaymentIntent(input: CreatePaymentIntentInput): PaymentIntentResponse {
        return client.post("$baseUrl/payment_intents") {
            setBody(input)
        }.body()
    }

    override suspend fun getPaymentIntent(paymentIntentId: String, clientKey: String?): PaymentIntentResponse {
        return client.get("$baseUrl/payment_intents/$paymentIntentId") {
            if (clientKey != null) {
                parameter("client_key", clientKey)
            }
        }.body()
    }

    override suspend fun attachPaymentIntent(
        paymentIntentId: String,
        input: AttachPaymentIntentInput
    ): PaymentIntentResponse {
        return client.post("$baseUrl/payment_intents/$paymentIntentId/attach") {
            setBody(input)
        }.body()
    }

    override suspend fun createWebhook(input: CreateWebhookInput): WebhookResponse {
        return client.post("$baseUrl/webhooks") {
            setBody(input)
        }.body()
    }

    override suspend fun getWebhook(webhookId: String): WebhookResponse {
        return client.get("$baseUrl/webhooks/$webhookId").body()
    }

    override suspend fun getWebhooks(): WebhooksResponse {
        return client.get("$baseUrl/webhooks").body()
    }

    override suspend fun disableWebhook(webhookId: String): WebhookResponse {
        return client.post("$baseUrl/webhooks/$webhookId/disable").body()
    }

    override suspend fun enabledWebhook(webhookId: String): WebhookResponse {
        return client.post("$baseUrl/webhooks/$webhookId/enable").body()
    }

    override suspend fun updateWebhook(webhookId: String, input: CreateWebhookInput): WebhookResponse {
        return client.put("$baseUrl/webhooks/$webhookId") {
            setBody(input)
        }.body()
    }

    companion object {
        private const val baseUrl = "https://api.paymongo.com/v1"
    }
}