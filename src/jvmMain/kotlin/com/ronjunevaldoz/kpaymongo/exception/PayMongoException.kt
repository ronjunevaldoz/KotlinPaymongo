package com.ronjunevaldoz.kpaymongo.exception

import com.ronjunevaldoz.kpaymongo.models.error.PayMongoError

class PayMongoException(val errors: List<PayMongoError>) : RuntimeException(
    errors.joinToString { "${it.code}: ${it.detail}" }
)