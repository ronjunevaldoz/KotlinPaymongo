package com.ronjunevaldoz.kpaymongo

import com.ronjunevaldoz.kpaymongo.models.Billing
import com.ronjunevaldoz.kpaymongo.models.resource.*
import kotlin.properties.Delegates


class PaymongoSourceBuilder {
    var type: Source.Type by Delegates.notNull()
    var amount: Int by Delegates.notNull()
    var redirectSuccess: String by Delegates.notNull()
    var redirectFailed: String by Delegates.notNull()
    var billing: Billing? = null
    var currency: String = "PHP"
    fun build() = CreateSourceInput(
        data = CreateSourceInput.SourceInput(
            attributes = CreateSourceInput.AttributesInput(
                type = type,
                amount = amount, // 100.00
                redirect = CreateSourceInput.RedirectInput(
                    success = redirectSuccess,
                    failed = redirectFailed
                ),
                billing = billing,
                currency = currency
            )
        )
    )
}

class PaymongoPaymentMethodBuilder {
    var type: PaymentMethod.Type by Delegates.notNull()
    var billing: Billing by Delegates.notNull()
    var cardNumber: String by Delegates.notNull()
    var expMonth: Int by Delegates.notNull()
    var expYear: Int by Delegates.notNull()
    var cvc: String by Delegates.notNull()
    var metadata: Map<String, String> = emptyMap()

    private fun buildDetails() = CreatePaymentMethodInput.CreatePaymentMethod.Details(
        cardNumber = cardNumber,
        expMonth = expMonth,
        expYear = expYear,
        cvc = cvc
    )

    fun build() = CreatePaymentMethodInput(
        data = CreatePaymentMethodInput.CreatePaymentMethod(
            attributes = CreatePaymentMethodInput.CreatePaymentMethod.Attributes(
                type = type,
                details = buildDetails(),
                billing = billing,
                metadata = metadata
            )
        )
    )
}

suspend fun IKPayMongoClient.createSource(input: PaymongoSourceBuilder.() -> Unit): SourceResponse {
    val builder = PaymongoSourceBuilder()
    input.invoke(builder)
    return createSource(builder.build())
}

suspend fun IKPayMongoClient.createPaymentMethod(input: PaymongoPaymentMethodBuilder.() -> Unit): PaymentMethodResponse {
    val builder = PaymongoPaymentMethodBuilder()
    input.invoke(builder)
    return createPaymentMethod(builder.build())
}