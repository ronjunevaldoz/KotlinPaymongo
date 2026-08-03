# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v1.2.0]

### Added
- New `paymongo-kotlin-ktor-server` artifact: a route-scoped Ktor plugin (`PayMongoWebhookVerification`) that verifies the `Paymongo-Signature` header and decodes the request body into a typed `ReceiveWebhookEvent`. Signature algorithm confirmed against PayMongo's official Node SDK source, not just docs prose.
- Opt-in webhook redelivery dedup (`PayMongoWebhookConfig.dedupStore`, default `InMemoryPayMongoWebhookDedupStore`) -- PayMongo retries webhook delivery on timeout/failure, so a handler can otherwise double-process the same event.

### Changed
- `Payment.Attributes.fee`/`.netAmount`/`.foreignFee`/`.taxAmount` and `Link.Attributes.fee`/`.taxAmount` are now `Amount` instead of raw `Int`, matching every other money field. Wire format unchanged.

### Investigated, not shipped
- `Idempotency-Key` is documented by PayMongo ("POST requests that create or modify data") but tested live against `/v1/payment_links` -- two identical requests with the same key created two different resources. Not wired into the client; would have shipped a false sense of duplicate-charge protection.

## [v1.1.0]

### Added
- Payment Links resource (`/v1/payment_links`): create, get, list, archive/unarchive, list payments, create refund
- Customers resource: create, retrieve, list, update, delete
- Customer payment methods (v2): list, delete
- Checkout session v2 (deferred flow, no Payment Intent created up front)
- `listPayments` (list all payments)
- `Amount` value class wrapping every `amount` field (Source, Payment, PaymentIntent, Link, PaymentLink, Refund, CheckoutSession, Tax) so pesos/centavos can't be mixed up at a call site; wire format unchanged. `Int.centavos`, `Double.majorUnits`/`.pesos`, `Amount.centavos`/`.pesos` literal/alias extensions

### Changed
- `Config.logLevel` now defaults to `LogLevel.NONE` (was `LogLevel.ALL`, which logged the secret key and payment bodies on every request)
- `createLink`/`getLink`/`getLinkByReference`/`archiveLink`/`unarchiveLink` deprecated in favor of the Payment Links equivalents; PayMongo has retired `/links` from its docs

### Fixed
- `PayMongoError.Source.attribute` was non-nullable, crashing with a confusing `MissingFieldException` instead of the intended `PayMongoException` on validation errors that omit it
- `Payment.Attributes.availableAt` was missing `@SerialName("available_at")` and always decoded to `0`
- `ResourceSerializer.selectDeserializer` dispatched on JSON key presence instead of the `type` field's value, so it never matched a real payload
- `ReceivedWebhookEventTest`'s assertions were silent no-ops (`assert()` is disabled by default on the JVM without `-ea`)

## [v1.0.3-dev02]
### Changes
- Bump to latest kotlin 2.2.21

### Fixes
- Fix pipeline error

## [v1.0.3-dev01]

### Fixes
- Fix publishing error due to permission denied on `./gradlew`

## [v1.0.3]

### Fixes
- Fix getWebhook returns `resource_not_found` due to a wrong path url

## [v1.0.2-dev07]

### Changes
- Update to ktor 3.0.1 and kotlin 2.1.0

## [v1.0.2-dev06]

### Fixes
- Fix link webhook response

## [v1.0.2-dev05]

### Fixes
- wrong input in createLink

## [v1.0.2-dev04]

### Added
- new qrph.expired event
- allow null in url webhook creation/update

## [v1.0.2-dev03]

### Changed
- Bump version to v1.0.2-dev03

## [v1.0.2-dev02]

### Changed
- Support link in Resource serializers

## [v1.0.2-dev01]

### Added
- Create a link
- Get link by id
- Get link by reference number
- Archive link
- Unarchive link
- Added new webhook `Event` `link.payment.paid` and `checkout_session.payment.paid`

## [v1.0.1]

### Changed
- Update kotlin version to 2.0.20
- Merge dev02

## [v1.0.0-dev02]

### Changed
- Optimize deprecated content from http response
- Refactor rename Paymongo to PayMongo

## [v1.0.0-dev01] 

### Changed
- change package name
- added compose multiplatform module

## [v1.0.0]

### Added
- Added samples demo project

### Changed
- Upgrade to multiplatform with WASMJS

[v1.0.2-dev02]: https://github.com/ronjunevaldoz/KotlinPaymongo/compare/v1.0.2-dev01...HEAD
[v1.0.2-dev01]: https://github.com/ronjunevaldoz/KotlinPaymongo/compare/v1.0.2-dev01...v1.0.2-dev02
[v1.0.1]: https://github.com/ronjunevaldoz/KotlinPaymongo/compare/v1.0.1...v1.0.2-dev01
[v1.0.0-dev02]: https://github.com/ronjunevaldoz/KotlinPaymongo/compare/v1.0.0-dev02...v1.0.1
[v1.0.0-dev01]: https://github.com/ronjunevaldoz/KotlinPaymongo/compare/v1.0.0-dev01...v1.0.0-dev02
[v1.0.0]: https://github.com/ronjunevaldoz/KotlinPaymongo/compare/v1.0.0...v1.0.0-dev01