package io.github.ronjunevaldoz.paymongo

import io.github.ronjunevaldoz.paymongo.models.resource.*


/**
 *  @see  (https://developers.paymongo.com/reference)
 */
interface IPaymongo {
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

    /**
     * New!
     * Checkout Session
     * https://developers.paymongo.com/reference/create-a-checkout
     */
    suspend fun createCheckoutSession(input: CreateCheckoutSessionInput): CheckoutSessionResponse
    suspend fun getCheckoutSession(checkoutSessionId: String): CheckoutSessionResponse
    suspend fun expireCheckoutSession(checkoutSessionId: String): CheckoutSessionResponse
}