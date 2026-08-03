package io.github.ronjunevaldoz.paymongo.ktor

import io.github.ronjunevaldoz.paymongo.models.resource.ReceiveWebhookEvent
import io.github.ronjunevaldoz.paymongo.serialization.PayMongoJson
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.request.header
import io.ktor.server.request.receiveText
import io.ktor.util.AttributeKey
import kotlin.properties.Delegates

class PayMongoWebhookConfig {
    /** The secret key PayMongo shows when the webhook endpoint was created. Required. */
    var secretKey: String by Delegates.notNull()

    /** Header PayMongo sends the signature in. Only override this for testing. */
    var signatureHeaderName: String = "Paymongo-Signature"

    /**
     * Opt-in redelivery dedup. Null (default) disables it -- [ApplicationCall.isDuplicatePayMongoEvent]
     * is always false in that case, since there's nothing to compare against.
     */
    var dedupStore: PayMongoWebhookDedupStore? = null
}

private val PayMongoEventKey = AttributeKey<ReceiveWebhookEvent>("PayMongoEvent")
private val PayMongoDuplicateEventKey = AttributeKey<Boolean>("PayMongoIsDuplicateEvent")

/**
 * Verifies the `Paymongo-Signature` header and parses the request body into a
 * [ReceiveWebhookEvent], storing it on [ApplicationCall.attributes] for the route handler to
 * read via [ApplicationCall.payMongoEvent].
 *
 * ```kotlin
 * routing {
 *     route("/webhooks/paymongo") {
 *         install(PayMongoWebhookVerification) {
 *             secretKey = "whsec_..."
 *             dedupStore = InMemoryPayMongoWebhookDedupStore() // opt-in, see class docs
 *         }
 *         post {
 *             if (call.isDuplicatePayMongoEvent) {
 *                 call.respond(HttpStatusCode.OK) // already processed, ack without reprocessing
 *                 return@post
 *             }
 *             when (call.payMongoEvent.data.attributes.type) {
 *                 WebhookEvent.Event.PaymentPaid -> { /* ... */ }
 *                 else -> {}
 *             }
 *             call.respond(HttpStatusCode.OK)
 *         }
 *     }
 * }
 * ```
 *
 * On a missing/malformed header this throws [PayMongoSignatureFormatException]; on a
 * signature mismatch it throws [PayMongoSignatureMismatchException]. Neither is caught here --
 * install Ktor's `StatusPages` plugin to map them to a `400 Bad Request` response, or they'll
 * surface as an unhandled `500`.
 */
val PayMongoWebhookVerification = createRouteScopedPlugin(
    name = "PayMongoWebhookVerification",
    createConfiguration = ::PayMongoWebhookConfig
) {
    onCall { call ->
        val rawBody = call.receiveText()
        val signatureHeader = call.request.header(pluginConfig.signatureHeaderName)
            ?: throw PayMongoSignatureFormatException(
                "Missing \"${pluginConfig.signatureHeaderName}\" header"
            )

        PayMongoWebhookVerifier.verify(rawBody, signatureHeader, pluginConfig.secretKey)

        val event = PayMongoJson.decodeFromString(ReceiveWebhookEvent.serializer(), rawBody)
        call.attributes.put(PayMongoEventKey, event)

        val isDuplicate = pluginConfig.dedupStore?.let { store -> !store.markSeen(event.data.id) } ?: false
        call.attributes.put(PayMongoDuplicateEventKey, isDuplicate)
    }
}

/**
 * The verified, decoded webhook event. Only valid on a route where [PayMongoWebhookVerification]
 * is installed -- throws if the plugin hasn't run.
 */
val ApplicationCall.payMongoEvent: ReceiveWebhookEvent
    get() = attributes[PayMongoEventKey]

/**
 * True if [PayMongoWebhookConfig.dedupStore] is configured and this event id was already seen.
 * Always false when no dedup store is configured -- there's nothing to compare against.
 */
val ApplicationCall.isDuplicatePayMongoEvent: Boolean
    get() = attributes.getOrNull(PayMongoDuplicateEventKey) ?: false
