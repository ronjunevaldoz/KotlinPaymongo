# KotlinPaymongo
Paymongo client for kotlin

![Build And Publish](https://github.com/ronjunevaldoz/KotlinPaymongo/actions/workflows/publish.yml/badge.svg)
![Maven Central Version](https://img.shields.io/maven-central/v/io.github.ronjunevaldoz/paymongo-kotlin)
[![Kotlin](https://img.shields.io/badge/kotlin-2.2.21-blue.svg?logo=kotlin)](http://kotlinlang.org)
![GitHub](https://img.shields.io/github/license/ronjunevaldoz/KotlinPaymongo)

## Feature

- Source
  - [x] Create source
  - [x] Get source by id
- Payments
  - [x] Create payment
  - [x] List all payments
  - [ ] Get payment by id -- not offered by the PayMongo API
- Payment Intents
  - [x] Create payment intent
  - [x] Get payment intent by id and or client key
  - [x] Attach payment intent
- Payment Method
  - [x] Create payment method
  - [ ] Get/update payment method by id -- not offered by the PayMongo API; only customer-scoped payment methods exist (see below)
  - [x] List a customer's payment methods (v2)
  - [x] Delete a customer's payment method (v2)
- Checkout Sessions
  - [x] Create checkout session (v1)
  - [x] Get / expire checkout session
  - [x] Create checkout session (v2, deferred flow, recommended for new integrations)
- Webhooks
  - [x] Create webhook
  - [x] Get webhook
  - [x] Get webhooks
  - [x] Enable/Disable webhook
  - [x] Update webhook
- Link (deprecated by PayMongo, use Payment Links instead)
  - [x] Create a link
  - [x] Get link by id
  - [x] Get link by reference number
  - [x] Archive link
  - [x] Unarchive link
- Payment Links
  - [x] Create a payment link
  - [x] Get a payment link by id
  - [x] List payment links
  - [x] Archive/Unarchive a payment link
  - [x] List payments for a payment link
- Refunds
  - [x] Create refund on a payment link
  - [ ] Retrieve/list refunds -- not offered by the PayMongo API
- Customers
  - [x] Create customer
  - [x] Retrieve a customer
  - [x] List customers
  - [x] Update customer
  - [x] Delete customer

## API Reference
https://docs.paymongo.com/reference

## Usage
```kotlin
val config = Paymongo.Config.apply{
   secretKey = "sk_123456"
}
val client = PayMongo(config)
```
## Ktor Server Webhook Plugin

`paymongo-kotlin-ktor-server` verifies the `Paymongo-Signature` header, decodes the payload, and
optionally flags redelivered events. See [samples/server](samples/server/src/main/java/io/github/ronjunevaldoz/paymongo/server/Application.kt)
for a full runnable example.

```kotlin
implementation("io.github.ronjunevaldoz:paymongo-kotlin-ktor-server:<VERSION>")
```

```kotlin
routing {
    route("/webhooks/paymongo") {
        install(PayMongoWebhookVerification) {
            secretKey = "whsec_..."
            dedupStore = InMemoryPayMongoWebhookDedupStore() // opt-in; process-local only
        }
        post {
            if (call.isDuplicatePayMongoEvent) {
                call.respond(HttpStatusCode.OK)
                return@post
            }
            when (call.payMongoEvent.data.attributes.type) {
                WebhookEvent.Event.PaymentPaid -> { /* fulfill the order */ }
                else -> {}
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}
```

A missing/malformed signature header throws `PayMongoSignatureFormatException`; a signature
mismatch throws `PayMongoSignatureMismatchException`. Install Ktor's `StatusPages` to map either
to a `400`, or they surface as an unhandled `500`.

## Installation
```kotlin
repositories {
   mavenCentral()
}
```

## Common Dependency
```kotlin
implementation("io.github.ronjunevaldoz:paymongo-kotlin:<VERSION>")
```
## Platform specific dependency (jvm, ios, android, wasmjs)
```kotlin
implementation("io.github.ronjunevaldoz:paymongo-kotlin-<PLATFORM>:<VERSION>")
```
## Related PayMongo community made libraries
https://developers.paymongo.com/docs/community-made-libraries