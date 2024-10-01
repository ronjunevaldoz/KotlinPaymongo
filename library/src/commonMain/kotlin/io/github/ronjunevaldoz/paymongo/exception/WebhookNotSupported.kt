package io.github.ronjunevaldoz.paymongo.exception

class WebhookNotSupported(override val message: String) : RuntimeException(message)