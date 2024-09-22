package com.ronjunevaldoz.kpaymongo

import com.ronjunevaldoz.kpaymongo.models.resource.*
import com.ronjunevaldoz.kpaymongo.util.PaymongoClientFactory
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.serialization.json.Json


class Paymongo(config: Config, private val client: HttpClient = PaymongoClientFactory.client(config)) : IPaymongo {

    override suspend fun createSource(input: CreateSourceInput): SourceResponse {
        return client.post("/sources") {
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

    class Config(
        var secretKey: String,
        var userAgent: String,
        var json: Json = PaymongoJson
    )
}