package io.github.ronjunevaldoz.paymongo

import io.github.ronjunevaldoz.paymongo.models.resource.AttachPaymentIntentInput
import io.github.ronjunevaldoz.paymongo.models.resource.CheckoutSessionResponse
import io.github.ronjunevaldoz.paymongo.models.resource.CreateCheckoutSessionInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreateCustomerInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreateLinkInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreatePaymentInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreatePaymentIntentInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreatePaymentLinkInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreatePaymentMethodInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreateRefundInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreateSourceInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreateWebhookInput
import io.github.ronjunevaldoz.paymongo.models.resource.CustomerResponse
import io.github.ronjunevaldoz.paymongo.models.resource.CustomersResponse
import io.github.ronjunevaldoz.paymongo.models.resource.DeletedCustomerResponse
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentIntentResponse
import io.github.ronjunevaldoz.paymongo.models.resource.Link
import io.github.ronjunevaldoz.paymongo.models.resource.LinkResponse
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentLinkResponse
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentLinksResponse
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentMethodResponse
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentResponse
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentsResponse
import io.github.ronjunevaldoz.paymongo.models.resource.RefundResponse
import io.github.ronjunevaldoz.paymongo.models.resource.SourceResponse
import io.github.ronjunevaldoz.paymongo.models.resource.UpdateCustomerInput
import io.github.ronjunevaldoz.paymongo.models.resource.UpdatePaymentLinkInput
import io.github.ronjunevaldoz.paymongo.models.resource.WebhookResponse
import io.github.ronjunevaldoz.paymongo.models.resource.WebhooksResponse
import io.github.ronjunevaldoz.paymongo.serialization.PayMongoJson
import io.github.ronjunevaldoz.paymongo.util.PayMongoClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.plugins.logging.LogLevel
import kotlinx.serialization.json.Json


class PayMongo(
    config: Config,
    private val client: HttpClient = PayMongoClientFactory.client(config)
) :
    IPayMongo {

    override suspend fun createSource(input: CreateSourceInput): SourceResponse {
        return client.post("sources") {
            setBody(input)
        }.body()
    }

    override suspend fun getSource(id: String): SourceResponse {
        return client.get("sources/$id").body()
    }

    override suspend fun createPayment(input: CreatePaymentInput): PaymentResponse {
        return client.post("payments") {
            setBody(input)
        }.body()
    }

    override suspend fun listPayments(
        limit: Int?,
        before: String?,
        after: String?
    ): PaymentsResponse {
        return client.get("payments") {
            limit?.let { parameter("limit", it) }
            before?.let { parameter("before", it) }
            after?.let { parameter("after", it) }
        }.body()
    }

    override suspend fun createPaymentMethod(input: CreatePaymentMethodInput): PaymentMethodResponse {
        return client.post("payment_methods") {
            setBody(input)
        }.body()
    }

    override suspend fun createPaymentIntent(input: CreatePaymentIntentInput): PaymentIntentResponse {
        return client.post("payment_intents") {
            setBody(input)
        }.body()
    }

    override suspend fun getPaymentIntent(
        paymentIntentId: String,
        clientKey: String?
    ): PaymentIntentResponse {
        return client.get("payment_intents/$paymentIntentId") {
            if (clientKey != null) {
                parameter("client_key", clientKey)
            }
        }.body()
    }

    override suspend fun attachPaymentIntent(
        paymentIntentId: String,
        input: AttachPaymentIntentInput
    ): PaymentIntentResponse {
        return client.post("payment_intents/$paymentIntentId/attach") {
            setBody(input)
        }.body()
    }

    override suspend fun createWebhook(input: CreateWebhookInput): WebhookResponse {
        return client.post("webhooks") {
            setBody(input)
        }.body()
    }

    override suspend fun getWebhook(webhookId: String): WebhookResponse {
        return client.get("webhooks/$webhookId").body()
    }

    override suspend fun getWebhooks(): WebhooksResponse {
        return client.get("webhooks").body()
    }

    override suspend fun disableWebhook(webhookId: String): WebhookResponse {
        return client.post("webhooks/$webhookId/disable").body()
    }

    override suspend fun enabledWebhook(webhookId: String): WebhookResponse {
        return client.post("webhooks/$webhookId/enable").body()
    }

    override suspend fun updateWebhook(
        webhookId: String,
        input: CreateWebhookInput
    ): WebhookResponse {
        return client.put("webhooks/$webhookId") {
            setBody(input)
        }.body()
    }

    override suspend fun createCheckoutSession(input: CreateCheckoutSessionInput): CheckoutSessionResponse {
        return client.post("checkout_sessions") {
            setBody(input)
        }.body()
    }

    override suspend fun getCheckoutSession(checkoutSessionId: String): CheckoutSessionResponse {
        return client.get("checkout_sessions/$checkoutSessionId").body()
    }

    override suspend fun expireCheckoutSession(checkoutSessionId: String): CheckoutSessionResponse {
        return client.post("checkout_sessions/$checkoutSessionId/expire").body()
    }

    @Deprecated("Retired by PayMongo; use createPaymentLink", ReplaceWith("createPaymentLink(input)"))
    override suspend fun createLink(input: CreateLinkInput): LinkResponse {
        return client.post("links") {
            setBody(input)
        }.body()
    }

    @Deprecated("Retired by PayMongo; use getPaymentLink", ReplaceWith("getPaymentLink(id)"))
    override suspend fun getLink(id: String): LinkResponse {
        return client.get("links/$id").body()
    }

    @Deprecated("Retired by PayMongo; no direct replacement, filter listPaymentLinks results")
    override suspend fun getLinkByReference(referenceNumber: String): LinkResponse {
        return client.get("links") {
            parameter("reference_number", referenceNumber)
        }.body()
    }

    @Deprecated("Retired by PayMongo; use updatePaymentLink(id, archive = true)", ReplaceWith("updatePaymentLink(id, true)"))
    override suspend fun archiveLink(id: String): LinkResponse {
        return client.post("links/$id/archive").body()
    }

    @Deprecated("Retired by PayMongo; use updatePaymentLink(id, archive = false)", ReplaceWith("updatePaymentLink(id, false)"))
    override suspend fun unarchiveLink(id: String): LinkResponse {
        return client.post("links/$id/unarchive").body()
    }

    override suspend fun createPaymentLink(input: CreatePaymentLinkInput): PaymentLinkResponse {
        return client.post("payment_links") {
            setBody(input)
        }.body()
    }

    override suspend fun getPaymentLink(id: String): PaymentLinkResponse {
        return client.get("payment_links/$id").body()
    }

    override suspend fun listPaymentLinks(
        limit: Int?,
        before: String?,
        after: String?
    ): PaymentLinksResponse {
        return client.get("payment_links") {
            limit?.let { parameter("limit", it) }
            before?.let { parameter("before", it) }
            after?.let { parameter("after", it) }
        }.body()
    }

    override suspend fun updatePaymentLink(id: String, archive: Boolean): PaymentLinkResponse {
        return client.patch("payment_links/$id") {
            setBody(
                UpdatePaymentLinkInput(
                    data = UpdatePaymentLinkInput.PaymentLinkInput(
                        attributes = UpdatePaymentLinkInput.Attributes(archive)
                    )
                )
            )
        }.body()
    }

    override suspend fun getPaymentLinkPayments(id: String): PaymentsResponse {
        return client.get("payment_links/$id/payments").body()
    }

    override suspend fun createPaymentLinkRefund(id: String, input: CreateRefundInput): RefundResponse {
        return client.post("payment_links/$id/refunds") {
            setBody(input)
        }.body()
    }

    override suspend fun createCustomer(input: CreateCustomerInput): CustomerResponse {
        return client.post("customers") {
            setBody(input)
        }.body()
    }

    override suspend fun getCustomer(id: String): CustomerResponse {
        return client.get("customers/$id").body()
    }

    override suspend fun listCustomers(
        limit: Int?,
        before: String?,
        after: String?
    ): CustomersResponse {
        return client.get("customers") {
            limit?.let { parameter("limit", it) }
            before?.let { parameter("before", it) }
            after?.let { parameter("after", it) }
        }.body()
    }

    override suspend fun updateCustomer(id: String, input: UpdateCustomerInput): CustomerResponse {
        return client.patch("customers/$id") {
            setBody(input)
        }.body()
    }

    override suspend fun deleteCustomer(id: String): DeletedCustomerResponse {
        return client.delete("customers/$id").body()
    }

    class Config(
        var secretKey: String,
        var userAgent: String = "Paymongo Kotlin Client",
        var json: Json = PayMongoJson,
        var logLevel: LogLevel = LogLevel.NONE
    )
}