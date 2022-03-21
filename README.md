# KotlinPaymongo
Paymongo client for kotlin

![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/io.github.ronjunevaldoz/kpaymongo-jvm?server=https%3A%2F%2Fs01.oss.sonatype.org)
[![Kotlin](https://img.shields.io/badge/kotlin-1.6.10-blue.svg?logo=kotlin)](http://kotlinlang.org)
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
- [ ] Enable/Disable webook

## Usage
```kotlin
val client = KPayMongoClient(<PAYMONGO_SECRET_KEY>)

// create a source via DSL
val source = client.createSource {
   type = Source.Type.GCash // Source.Type.GrabPay
   amount = 10000 // 100.00
   redirectSuccess = successUrl
   redirectFailed = failedUrl
   billing = Billing(
      name = "Full name",
      phone = "09xxxxxxxxx",
      email = "sample@email.com,
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

## Installation
```kotlin
repositories { 
    mavenCentral()
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots") }
}
```

## Dependency
```kotlin
implementation("io.github.ronjunevaldoz:kpaymongo-jvm:1.0-SNAPSHOT")
```
