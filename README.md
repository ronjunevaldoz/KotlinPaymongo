# KotlinPaymongo
Paymongo client for kotlin

![Build And Publish](https://github.com/ronjunevaldoz/KotlinPaymongo/actions/workflows/publish.yml/badge.svg)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/io.github.ronjunevaldoz/payomongo-kotlin?server=https%3A%2F%2Fs01.oss.sonatype.org)
[![Kotlin](https://img.shields.io/badge/kotlin-2.0.20-Beta2-blue.svg?logo=kotlin)](http://kotlinlang.org)
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
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots") }
}
```

## Dependency
```kotlin
implementation("io.github.ronjunevaldoz:kpaymongo-jvm:1.0.0-SNAPSHOT"){
  isChanging = true // this will allow to get all latest changes
}
```

## How do I deploy it to Maven Central?

The most part of the job is already automated for you. However, deployment to Maven Central requires some manual work from your side.

1. - [ ] Create an account at [Sonatype issue tracker](https://issues.sonatype.org/secure/Signup!default.jspa)
2. - [ ] [Create an issue](https://issues.sonatype.org/secure/CreateIssue.jspa?issuetype=21&pid=10134) to create new project for you
3. - [ ] You will have to prove that you own your desired namespace
4. - [ ] Create a GPG key with `gpg --gen-key`, use the same email address you used to sign up to the Sonatype Jira
5. - [ ] Find your key id in the output of the previous command looking like `D89FAAEB4CECAFD199A2F5E612C6F735F7A9A519`
6. - [ ] Upload your key to a keyserver, for example
 ```bash
 gpg --send-keys --keyserver keyserver.ubuntu.com "<your key id>"
 ```
1. - [ ] Now you should create secrets available to your GitHub Actions
    1. via `gh` command

 ```bash
 gh secret set SIGNING_KEY -a actions --body "$(gpg --export-secret-key --armor "<KEY_ID>")"
 gh secret set SIGNING_KEY_ID -a actions --body "<KEY_ID>"
 gh secret set SIGNING_KEY_PASSWORD -a actions --body "<SECRET_PASSWORD>"
 gh secret set MAVEN_CENTRAL_PASSWORD -a actions --body "<GENERATED_PASSWORD>"
 gh secret set MAVEN_CENTRAL_USERNAME -a actions --body "<GENERATED_USERNAME>"
 ```

1- [ ] Deploy command
`./gradlew publishAndReleaseToMavenCentral --no-configuration-cache`          
