package com.ronjunevaldoz.kpaymongo

import com.ronjunevaldoz.kpaymongo.models.Billing
import com.ronjunevaldoz.kpaymongo.models.resource.*
import kotlin.properties.Delegates


class PaymongoSourceBuilder {
    var type: PaymentType by Delegates.notNull()
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

suspend fun IPaymongo.createSource(input: PaymongoSourceBuilder.() -> Unit): SourceResponse {
    val builder = PaymongoSourceBuilder()
    input.invoke(builder)
    return createSource(builder.build())
}

suspend fun IPaymongo.createPaymentMethod(input: CreatePaymentMethodInput.Builder.() -> Unit): PaymentMethodResponse {
    return createPaymentMethod(CreatePaymentMethodInput.createPaymentMethodInput{
        apply(input)
    })
}