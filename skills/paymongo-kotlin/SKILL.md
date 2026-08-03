---
name: paymongo-kotlin
description: >
  Use the paymongo-kotlin (io.github.ronjunevaldoz:paymongo-kotlin) Kotlin Multiplatform
  client to call the PayMongo API -- Payment Links, Customers, Refunds, Checkout Sessions,
  Sources, Payments, Payment Intents, Payment Methods, Webhooks. Covers request/response
  body conventions (they differ by endpoint family -- getting this wrong is the most common
  integration bug), the Amount type for money fields, the deprecated Link resource and its
  replacement, and webhook event handling. Load this before writing any code that calls
  PayMongo through this library.
---

# paymongo-kotlin

Kotlin Multiplatform client for the [PayMongo API](https://docs.paymongo.com/reference).
Targets: JVM, Android, iOS (x64/arm64/simulatorArm64), wasmJs, linuxX64.

```kotlin
implementation("io.github.ronjunevaldoz:paymongo-kotlin:1.1.0")
// or a platform-specific artifact: paymongo-kotlin-<jvm|ios|android|wasmjs>
```

## Quickstart

```kotlin
val client = PayMongo(
    config = PayMongo.Config(secretKey = "sk_test_...")
    // logLevel defaults to LogLevel.NONE -- the secret key and payment bodies are
    // never logged unless you explicitly opt in
)

val link = client.createPaymentLink(
    CreatePaymentLinkInput(amount = 100.0.pesos, currency = "PHP", description = "Order #123")
)
println(link.data.url) // share this with the customer
```

## The #1 gotcha: two different request/response conventions

PayMongo's endpoints split into two families with **different body shapes**. Mixing them up
is the most common integration bug -- it's easy to assume every endpoint works like the
first one you tried.

**Older resources** (`Source`, `Payment`, `PaymentIntent`, `Webhook`, `CheckoutSession` v1,
`Customer`, deprecated `Link`): nested `{"data": {"attributes": {...}}}` on write, epoch
Long timestamps on read.

**Newer resources** (`PaymentLink`, `Refund`, Checkout Session v2, Customer Payment Methods
v2): **flat** body -- no `data`/`attributes` wrapping at all -- and ISO-8601 timestamp
*strings*, not epoch. Verified against a live sandbox call; this is not a doc-summary guess.

```kotlin
// flat family -- PaymentLink/Refund
CreatePaymentLinkInput(amount = ..., currency = "PHP")           // no wrapping
client.createPaymentLinkRefund(linkId, CreateRefundInput(...))    // no wrapping

// nested family -- Customer
CreateCustomerInput(
    data = CreateCustomerInput.CustomerInput(
        attributes = CreateCustomerInput.Attributes(firstName = ..., email = ...)
    )
)
```

If a request comes back `400 invalid_request_body` with fields reported "required" even
though you supplied them, you almost certainly used the wrong wrapping for that endpoint.

## Amount -- never pass a raw Int/Double for money

Every amount field across the library is the `Amount` value class, not `Int`/`Double`.
PayMongo's wire format is unchanged (still a plain JSON integer in centavos) -- `Amount`
only prevents mixing up major and minor units at the call site.

```kotlin
1000.centavos        // Amount(1000) -- PHP 10.00
100.0.pesos           // Amount from major units -- same as 10000.centavos
amount.pesos          // Amount -> Double, major units
amount.centavos        // Amount -> Int, minor units (same as amount.minorUnits)
Amount.ZERO
amountA + amountB      // arithmetic and Comparable work directly on Amount
```

`centavos`/`pesos` are domestic aliases for `minorUnits`/`majorUnits` -- PayMongo only
supports PHP today, so either naming works; prefer whichever reads clearer at the call site.

## Resource reference

| Resource | Body convention | Timestamps | Notes |
|---|---|---|---|
| Source | nested | epoch Long | |
| Payment | nested | epoch Long | `listPayments()` supports `limit`/`before`/`after` |
| PaymentIntent | nested | epoch Long | `getPaymentIntent(id, clientKey?)` -- omit `clientKey` server-side |
| PaymentMethod | nested | -- | create only; no standalone get/update in the PayMongo API |
| Webhook | nested | epoch Long | |
| CheckoutSession (v1) | nested | epoch Long | `createCheckoutSession`/`getCheckoutSession`/`expireCheckoutSession` |
| CheckoutSession (v2) | flat | epoch Long | `createCheckoutSessionV2` -- deferred flow, no Payment Intent created up front; PayMongo recommends this for new integrations, track payment via the `checkout_session.payment.paid` webhook |
| Link | nested | epoch Long | **deprecated** -- PayMongo retired `/links` from its docs |
| PaymentLink | flat | ISO-8601 string | replaces `Link`; `createPaymentLink`/`getPaymentLink`/`listPaymentLinks`/`updatePaymentLink(id, archive: Boolean)`/`getPaymentLinkPayments` |
| Refund | flat | ISO-8601 string | `createPaymentLinkRefund` only -- PayMongo has no retrieve/list refund endpoint |
| Customer | nested | epoch Long | full CRUD |
| Customer Payment Methods (v2) | flat | epoch Long | `listCustomerPaymentMethods`/`deleteCustomerPaymentMethod` -- PayMongo has no standalone `/payment_methods/{id}` endpoint, only customer-scoped ones. Confirmed live: a customer with zero payment methods returns `404 resource_not_found`, not an empty list -- catch `PayMongoException`, don't treat it as a bug |

## Not offered by the PayMongo API (not a gap in this library)

- Retrieve or list a refund
- Get a payment by id
- Get or update a payment method by id (only customer-scoped payment methods exist)

## Link is deprecated -- migrate to PaymentLink

```kotlin
// old, deprecated
client.getLink(id)
client.archiveLink(id)

// new
client.getPaymentLink(id)
client.updatePaymentLink(id, archive = true)
```

`getLinkByReference` has no replacement -- PayMongo's v1 payment_links API has no
lookup-by-reference-number endpoint; filter `listPaymentLinks()` results client-side instead.

## Webhooks

`ReceiveWebhookEvent` decodes an incoming webhook payload. The event's `data` field is a
polymorphic `Resource` dispatched on the JSON `"type"` value (`payment`, `link`, `source`,
`webhook`, `payment_intent`, `payment_method`, `checkout_session`, `customer`).

```kotlin
val event = PayMongoJson.decodeFromString<ReceiveWebhookEvent>(requestBody)
when (event.data.attributes.type) {
    WebhookEvent.Event.PaymentPaid -> { /* ... */ }
    WebhookEvent.Event.LinkPaymentPaid -> { /* ... */ }
    else -> {}
}
```

## Errors

Every non-2xx response throws `PayMongoException(errors: List<PayMongoError>)`. Each
`PayMongoError` has `code`, `detail`, and an optional `source` (`pointer`, `attribute` --
`attribute` is frequently absent, don't assume it's always present).

```kotlin
try {
    client.createPaymentLink(input)
} catch (e: PayMongoException) {
    e.errors.forEach { println("${it.code}: ${it.detail}") }
}
```
