package io.github.ronjunevaldoz.paymongo.exception

import io.github.ronjunevaldoz.paymongo.models.error.PayMongoError

class PayMongoException(val errors: List<PayMongoError>) : RuntimeException(
    errors.joinToString { "${it.code}: ${it.detail}" }
)