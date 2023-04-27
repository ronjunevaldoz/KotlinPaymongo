package com.ronjunevaldoz.kpaymongo

import com.ronjunevaldoz.kpaymongo.exception.PayMongoException
import com.ronjunevaldoz.kpaymongo.models.error.PayMongoErrorResponse
import com.ronjunevaldoz.kpaymongo.models.resource.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
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
import kotlinx.serialization.json.Json

@OptIn(InternalAPI::class)
actual class Paymongo actual constructor(config: Config) : IPaymongo {
    private val client : HttpClient
    init {
        config.engine = DefaultConfig.engine
        config.json = DefaultConfig.json
        client = HttpClient(config.engine!!) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(UserAgent) {
                agent = config.userAgent
            }
            install(ContentNegotiation) {
                json(config.json ?: PaymongoJson)
            }
            install(Auth) {
                basic {
                    sendWithoutRequest { true }
                    credentials {
                        BasicAuthCredentials(username = config.secretKey, password = "")
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

                handleResponseExceptionWithRequest { cause: Throwable, request: HttpRequest ->
                    val error = when (cause) {
                        is ClientRequestException -> getError(cause.response.content)
                        else -> throw cause
                    }
                    throw PayMongoException(error.errors)
                }
            }
            defaultRequest {
                host = API_V1
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Accept, "application/json")
            }
        }
    }

    private suspend fun getError(responseContent: ByteReadChannel): PayMongoErrorResponse {
        responseContent.readUTF8Line()?.let {
            return PaymongoJson.decodeFromString(it)
        }
        throw IllegalArgumentException("not a parsable error")
    }

    override suspend fun createSource(input: CreateSourceInput): SourceResponse {
        return client.post("$API_V1/sources") {
            setBody(input)
        }.body()
    }

    override suspend fun getSource(id: String): SourceResponse {
        return client.get("/sources/$id").body()
    }

    override suspend fun createPayment(input: CreatePaymentInput): PaymentResponse {
        return client.post("/payments") {
            setBody(input)
        }.body()
    }

    override suspend fun createPaymentMethod(input: CreatePaymentMethodInput): PaymentMethodResponse {
        return client.post("/payment_methods") {
            setBody(input)
        }.body()
    }

    override suspend fun createPaymentIntent(input: CreatePaymentIntentInput): PaymentIntentResponse {
        return client.post("/payment_intents") {
            setBody(input)
        }.body()
    }

    override suspend fun getPaymentIntent(paymentIntentId: String, clientKey: String?): PaymentIntentResponse {
        return client.get("/payment_intents/$paymentIntentId") {
            if (clientKey != null) {
                parameter("client_key", clientKey)
            }
        }.body()
    }

    override suspend fun attachPaymentIntent(
        paymentIntentId: String,
        input: AttachPaymentIntentInput
    ): PaymentIntentResponse {
        return client.post("/payment_intents/$paymentIntentId/attach") {
            setBody(input)
        }.body()
    }

    override suspend fun createWebhook(input: CreateWebhookInput): WebhookResponse {
        return client.post("/webhooks") {
            setBody(input)
        }.body()
    }

    override suspend fun getWebhook(webhookId: String): WebhookResponse {
        return client.get("/webhooks/$webhookId").body()
    }

    override suspend fun getWebhooks(): WebhooksResponse {
        return client.get("/webhooks").body()
    }

    override suspend fun disableWebhook(webhookId: String): WebhookResponse {
        return client.post("/webhooks/$webhookId/disable").body()
    }

    override suspend fun enabledWebhook(webhookId: String): WebhookResponse {
        return client.post("/webhooks/$webhookId/enable").body()
    }

    override suspend fun updateWebhook(webhookId: String, input: CreateWebhookInput): WebhookResponse {
        return client.put("/webhooks/$webhookId") {
            setBody(input)
        }.body()
    }

    override suspend fun createCheckoutSession(input: CreateCheckoutSessionInput): CheckoutSessionResponse {
        return client.post("/checkout_sessions") {
            setBody(input)
        }.body()
    }

    override suspend fun getCheckoutSession(checkoutSessionId: String): CheckoutSessionResponse {
        return client.get("/checkout_sessions/$checkoutSessionId").body()
    }

    override suspend fun expireCheckoutSession(checkoutSessionId: String): CheckoutSessionResponse {
        return client.post("/checkout_sessions/$checkoutSessionId/expire").body()
    }

    companion object {
        val DefaultConfig = Config(
            engine = CIO.create(),
            json = PaymongoJson
        )
        private const val API_V1 = "https://api.paymongo.com/v1"
    }

    actual class Config actual constructor(
        var secretKey: String,
        var userAgent: String,
        var engine: HttpClientEngine?,
        var json: Json?
    )
}