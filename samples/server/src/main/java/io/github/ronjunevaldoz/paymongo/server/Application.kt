package io.github.ronjunevaldoz.paymongo.server

import io.github.ronjunevaldoz.paymongo.Paymongo

suspend fun main() {
    val paymongo = Paymongo(Paymongo.Config(
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