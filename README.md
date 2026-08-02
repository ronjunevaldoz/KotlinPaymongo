# KotlinPaymongo
Paymongo client for kotlin

![Build And Publish](https://github.com/ronjunevaldoz/KotlinPaymongo/actions/workflows/publish.yml/badge.svg)
![Maven Central Version](https://img.shields.io/maven-central/v/io.github.ronjunevaldoz/paymongo-kotlin)
[![Kotlin](https://img.shields.io/badge/kotlin-2.0.20-blue.svg?logo=kotlin)](http://kotlinlang.org)
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
## Ktor Webhook Integration
https://github.com/ronjunevaldoz/KotlinPaymongo/wiki/Ktor-Webhook

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