package io.github.ronjunevaldoz.paymongo

import io.github.ronjunevaldoz.paymongo.models.resource.AttachPaymentIntentInput
import io.github.ronjunevaldoz.paymongo.models.resource.CheckoutSessionResponse
import io.github.ronjunevaldoz.paymongo.models.resource.CreateCheckoutSessionInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreateLinkInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreatePaymentInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreatePaymentIntentInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreatePaymentMethodInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreateSourceInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreateWebhookInput
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentIntentResponse
import io.github.ronjunevaldoz.paymongo.models.resource.Link
import io.github.ronjunevaldoz.paymongo.models.resource.LinkResponse
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentMethodResponse
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentResponse
import io.github.ronjunevaldoz.paymongo.models.resource.SourceResponse
import io.github.ronjunevaldoz.paymongo.models.resource.WebhookResponse
import io.github.ronjunevaldoz.paymongo.models.resource.WebhooksResponse
import io.github.ronjunevaldoz.paymongo.serialization.PayMongoJson
import io.github.ronjunevaldoz.paymongo.util.PayMongoClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.serialization.json.Json


class PayMongo(
    config: Config,
    private val client: HttpClient = PayMongoClientFactory.client(config)
) :
    IPayMongo {

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

    override suspend fun getPaymentIntent(
        paymentIntentId: String,
        clientKey: String?
    ): PaymentIntentResponse {
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

    override suspend fun updateWebhook(
        webhookId: String,
        input: CreateWebhookInput
    ): WebhookResponse {
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

    override suspend fun createLink(input: CreateLinkInput): LinkResponse {
        return client.post("/links") {
            setBody(input)
        }.body()
    }

    override suspend fun getLink(id: String): LinkResponse {
        return client.get("/links/$id").body()
    }

    override suspend fun getLinkByReference(referenceNumber: String): LinkResponse {
        return client.get("/links") {
            parameter("reference_number", referenceNumber)
        }.body()
    }

    override suspend fun archiveLink(id: String): LinkResponse {
        return client.post("/links/$id/archive").body()
    }

    override suspend fun unarchiveLink(id: String): LinkResponse {
        return client.post("/links/$id/unarchive").body()
    }

    class Config(
        var secretKey: String,
        var userAgent: String = "Paymongo Kotlin Client",
        var json: Json = PayMongoJson
    )
}