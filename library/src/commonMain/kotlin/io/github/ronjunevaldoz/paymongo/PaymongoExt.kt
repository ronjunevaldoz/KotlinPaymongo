package io.github.ronjunevaldoz.paymongo

import io.github.ronjunevaldoz.paymongo.models.Billing
import io.github.ronjunevaldoz.paymongo.models.resource.*
import kotlin.properties.Delegates


class PayMongoSourceBuilder {
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

suspend fun IPayMongo.createSource(input: PayMongoSourceBuilder.() -> Unit): SourceResponse {
    val builder = PayMongoSourceBuilder()
    input.invoke(builder)
    return createSource(builder.build())
}

suspend fun IPayMongo.createPaymentMethod(input: CreatePaymentMethodInput.Builder.() -> Unit): PaymentMethodResponse {
    return createPaymentMethod(CreatePaymentMethodInput.createPaymentMethodInput {
        apply(input)
    })
}