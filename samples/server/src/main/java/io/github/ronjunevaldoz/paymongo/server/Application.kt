package io.github.ronjunevaldoz.paymongo.server

import io.github.ronjunevaldoz.paymongo.PayMongo

suspend fun main() {
    val paymongo = PayMongo(PayMongo.Config(
        secretKey = "sk_test_xxxxxxxx"
    ))
    runCatching {
        paymongo.getWebhooks()
    }.fold(
        onSuccess = {
            println(it.data)
        },
        onFailure = {
            it.printStackTrace()
        }
    )
}