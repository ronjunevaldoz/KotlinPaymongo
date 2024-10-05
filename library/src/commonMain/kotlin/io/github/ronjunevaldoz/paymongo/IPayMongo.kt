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


/**
 *  @see  (https://developers.paymongo.com/reference)
 */
interface IPayMongo {
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
    suspend fun getPaymentIntent(
        paymentIntentId: String,
        clientKey: String? = null
    ): PaymentIntentResponse

    /**
     *  @param [paymentIntentId] PaymentIntent id
     */
    suspend fun attachPaymentIntent(
        paymentIntentId: String,
        input: AttachPaymentIntentInput
    ): PaymentIntentResponse

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

    /**
     * New!
     * Checkout Session
     * https://developers.paymongo.com/reference/create-a-checkout
     */
    suspend fun createCheckoutSession(input: CreateCheckoutSessionInput): CheckoutSessionResponse
    suspend fun getCheckoutSession(checkoutSessionId: String): CheckoutSessionResponse
    suspend fun expireCheckoutSession(checkoutSessionId: String): CheckoutSessionResponse

    /**
     * New!
     * Create a Link
     * https://developers.paymongo.com/reference/create-a-link
     */
    suspend fun createLink(input: CreateLinkInput): LinkResponse
    suspend fun getLink(id: String): LinkResponse
    suspend fun getLinkByReference(referenceNumber: String): LinkResponse
    suspend fun archiveLink(id: String): LinkResponse
    suspend fun unarchiveLink(id: String): LinkResponse
}