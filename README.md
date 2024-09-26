# KotlinPaymongo
Paymongo client for kotlin

![Build And Publish](https://github.com/ronjunevaldoz/KotlinPaymongo/actions/workflows/publish.yml/badge.svg)
![Maven Central Version](https://img.shields.io/maven-central/v/io.github.ronjunevaldoz/paymongo-kotlin)
[![Kotlin](https://img.shields.io/badge/kotlin-2.0.20-blue.svg?logo=kotlin)](http://kotlinlang.org)
![GitHub](https://img.shields.io/github/license/ronjunevaldoz/KotlinPaymongo)

## Feature

- [x] [Create Source](README.md#Usage) - DSL supported
- [x] Create payment
- [x] Create payment method - DSL supported
- [x] Create payment intent
- [x] Create webhook
- [x] Get source by id
- [ ] Get payment by id
- [ ] Get payment method by id
- [x] Get payment intent by id and or client key
- [x] Attach payment intent
- [x] Get webhook
- [x] Get webhooks
- [ ] Enable/Disable webhook

## API Reference
https://developers.paymongo.com/reference

## Usage
```kotlin
val config = Paymongo.DefaultConfig.apply{
   secretKey = "sk_123456"
}
val client = Paymongo(config)

// create a source via DSL
val source = client.createSource {
   type = Source.Type.GCash // Source.Type.GrabPay
   amount = 10000 // 100.00
   redirectSuccess = successUrl
   redirectFailed = failedUrl
   billing = Billing(
      name = "Full name",
      phone = "09xxxxxxxxx",
      email = "sample@email.com",
      address = Address(
         line1 = "",
         line2 = "",
         state = "",
         postalCode = "",
         city = "",
         country = ""
      )
   )
}
// TODO - more sample 
```
## Ktor Webhook Integration
```kotlin 
// http://localhost/paymongo/events
fun Route.payMongo() {
   route("paymongo") {
      post("events") {
         processWebhookEvent(call)
      }
   }
}
// sample webhook process
// see instruction how to secure webhook 
// https://developers.paymongo.com/docs/creating-webhook#3-securing-a-webhook-optional-but-highly-recommended
suspend fun processWebhookEvent(call: ApplicationCall) {
   val jsonString = call.receiveText()
   val webhookEvent = AppJson.decodeFromString<ReceiveWebhookEvent>(jsonString)
   val signature = call.request.header("Paymongo-Signature")
   if (signature == null) {
      call.respond(HttpStatusCode.Unauthorized, "Missing Paymongo-Signature")
   } else {
      // signature verification
      val sign = signature.split(",")
      val t = sign[0].replace("t=", "").toLong() // timestamp
      val te = sign[1].replace("te=", "") // test mode
      val li = sign[2].replace("li=", "") // live mode
      val attributes = webhookEvent.data.attributes
      val liveMode = attributes.liveMode
      val signatureFromPayload = "$t.$jsonString"
      val signatureFromHeader = if (liveMode) {
         li
      } else {
         te
      }
      val webhookSecretKey = "" // created webhook secret key
      // hash signature
      val hashedSignature = hash(content = signatureFromPayload, key = webhookSecretKey, algorithm = "HmacSHA256")
      // check for a matching signature
      if (hashedSignature == signatureFromHeader) {
         // process event response here
         when (val data = attributes.data) {
            is Source -> {}
            is Payment -> {}
            else -> {}
         }
         call.respond(HttpStatusCode.OK, "Webhook OK")
      } else {
         call.respond(HttpStatusCode.Unauthorized, "Invalid signature")
      }
   }
}

```

## Installation
```kotlin
repositories {
   mavenCentral()
}
```

## Common Dependency
```kotlin
implementation("io.github.ronjunevaldoz:paymongo-kotlin:1.0.0")
```
## Platform specific dependency (jvm, ios, android, wasmjs)
```kotlin
implementation("io.github.ronjunevaldoz:paymongo-kotlin-<PLATFORM>:1.0.0")
```