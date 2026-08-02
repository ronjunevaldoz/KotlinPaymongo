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

    /**
     *  @param [limit] Max resources to return; defaults to 10 server-side
     *  @param [before] Pagination cursor for the previous page
     *  @param [after] Pagination cursor for the next page
     */
    suspend fun listPayments(
        limit: Int? = null,
        before: String? = null,
        after: String? = null
    ): PaymentsResponse

    // payment methods
    /**
     * A PaymentMethod resource describes which payment method was used to fulfill a payment. It is used with a PaymentIntent to collect payments.
     */
    suspend fun createPaymentMethod(input: CreatePaymentMethodInput): PaymentMethodResponse

    //     payment intents
    suspend fun createPaymentIntent(input: CreatePaymentIntentInput): PaymentIntentResponse

    /**
     *  @param [paymentIntentId] PaymentIntent id
     *  @param [clientKey] Client key used to authorize a client-side request; omit for a server-side request
     */
    suspend fun getPaymentIntent(
        paymentIntentId: String,
        clientKey: String? = null
    ): PaymentIntentResponse

    /**
     *  @param [paymentIntentId] PaymentIntent id
     *  @param [input] Payment method and client key to attach to the PaymentIntent
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
     * PayMongo retired the `/links` endpoint these target from its docs.
     * Use [createPaymentLink] and friends against `/v1/payment_links` instead.
     */
    @Deprecated("Retired by PayMongo; use createPaymentLink", ReplaceWith("createPaymentLink(input)"))
    suspend fun createLink(input: CreateLinkInput): LinkResponse

    @Deprecated("Retired by PayMongo; use getPaymentLink", ReplaceWith("getPaymentLink(id)"))
    suspend fun getLink(id: String): LinkResponse

    @Deprecated("Retired by PayMongo; no direct replacement, filter listPaymentLinks results")
    suspend fun getLinkByReference(referenceNumber: String): LinkResponse

    @Deprecated("Retired by PayMongo; use updatePaymentLink(id, archive = true)", ReplaceWith("updatePaymentLink(id, true)"))
    suspend fun archiveLink(id: String): LinkResponse

    @Deprecated("Retired by PayMongo; use updatePaymentLink(id, archive = false)", ReplaceWith("updatePaymentLink(id, false)"))
    suspend fun unarchiveLink(id: String): LinkResponse

    // payment links (current resource, replaces the legacy Link endpoints above)
    /**
     * Create a Payment Link
     * @see (https://docs.paymongo.com/reference/post_v1-payment-links)
     */
    suspend fun createPaymentLink(input: CreatePaymentLinkInput): PaymentLinkResponse

    /**
     *  @param [id] PaymentLink id
     */
    suspend fun getPaymentLink(id: String): PaymentLinkResponse

    /**
     *  @param [limit] Max resources to return; defaults to 10 server-side
     *  @param [before] Pagination cursor for the previous page
     *  @param [after] Pagination cursor for the next page
     */
    suspend fun listPaymentLinks(
        limit: Int? = null,
        before: String? = null,
        after: String? = null
    ): PaymentLinksResponse

    /**
     *  @param [id] PaymentLink id
     *  @param [archive] true to archive, false to unarchive
     */
    suspend fun updatePaymentLink(id: String, archive: Boolean): PaymentLinkResponse

    /**
     *  @param [id] PaymentLink id
     */
    suspend fun getPaymentLinkPayments(id: String): PaymentsResponse

    /**
     *  @param [id] PaymentLink id
     *  @param [input] Refund amount, payment id, and reason
     */
    suspend fun createPaymentLinkRefund(id: String, input: CreateRefundInput): RefundResponse

    // customers
    suspend fun createCustomer(input: CreateCustomerInput): CustomerResponse

    /**
     *  @param [id] Customer id
     */
    suspend fun getCustomer(id: String): CustomerResponse

    /**
     *  @param [limit] Max resources to return; defaults to 10 server-side
     *  @param [before] Pagination cursor for the previous page
     *  @param [after] Pagination cursor for the next page
     */
    suspend fun listCustomers(
        limit: Int? = null,
        before: String? = null,
        after: String? = null
    ): CustomersResponse

    /**
     *  @param [id] Customer id
     *  @param [input] Fields to update
     */
    suspend fun updateCustomer(id: String, input: UpdateCustomerInput): CustomerResponse

    /**
     *  @param [id] Customer id
     */
    suspend fun deleteCustomer(id: String): DeletedCustomerResponse
}