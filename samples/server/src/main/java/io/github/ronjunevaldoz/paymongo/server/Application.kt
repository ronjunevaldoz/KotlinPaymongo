package io.github.ronjunevaldoz.paymongo.server

import io.github.ronjunevaldoz.paymongo.ktor.InMemoryPayMongoWebhookDedupStore
import io.github.ronjunevaldoz.paymongo.ktor.PayMongoWebhookVerification
import io.github.ronjunevaldoz.paymongo.models.resource.WebhookEvent
import io.github.ronjunevaldoz.paymongo.ktor.isDuplicatePayMongoEvent
import io.github.ronjunevaldoz.paymongo.ktor.payMongoEvent
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun main() {
    val webhookSecretKey = System.getenv("PAYMONGO_WEBHOOK_SECRET") ?: "whsec_replace_me"

    embeddedServer(CIO, port = 8080) {
        routing {
            route("/webhooks/paymongo") {
                install(PayMongoWebhookVerification) {
                    secretKey = webhookSecretKey
                    // process-local only; swap in a shared store for a multi-instance deployment
                    dedupStore = InMemoryPayMongoWebhookDedupStore()
                }
                post {
                    if (call.isDuplicatePayMongoEvent) {
                        call.respond(HttpStatusCode.OK)
                        return@post
                    }

                    when (call.payMongoEvent.data.attributes.type) {
                        WebhookEvent.Event.PaymentPaid -> {
                            // fulfill the order tied to call.payMongoEvent.data.attributes.data
                        }
                        WebhookEvent.Event.PaymentFailed -> {
                            // notify the customer, release any held inventory
                        }
                        else -> {}
                    }

                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }.start(wait = true)
}
