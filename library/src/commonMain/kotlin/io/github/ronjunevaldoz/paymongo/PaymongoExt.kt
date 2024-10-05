package io.github.ronjunevaldoz.paymongo

import io.github.ronjunevaldoz.paymongo.models.Billing
import io.github.ronjunevaldoz.paymongo.models.resource.CreateLinkInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreatePaymentMethodInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreateSourceInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreateWebhookInput
import io.github.ronjunevaldoz.paymongo.models.resource.Link
import io.github.ronjunevaldoz.paymongo.models.resource.LinkResponse
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentMethodResponse
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentType
import io.github.ronjunevaldoz.paymongo.models.resource.SourceResponse
import io.github.ronjunevaldoz.paymongo.models.resource.WebhookEvent
import io.github.ronjunevaldoz.paymongo.models.resource.WebhookResponse
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

suspend fun IPayMongo.createWebhook(
    url: String? = null,
    events: List<WebhookEvent.Event> = WebhookEvent.Event.All
): WebhookResponse {
    return createWebhook(
        CreateWebhookInput(
            CreateWebhookInput.WebhookInput(
                CreateWebhookInput.AttributesInput(
                    url = url,
                    events = events
                )
            )
        )
    )
}


suspend fun IPayMongo.createLink(
    amount: Int,
    description: String,
    remarks: String
): LinkResponse {
    return createLink(
        CreateLinkInput(
            data = CreateLinkInput.LinkInput(
                attributes = CreateLinkInput.Attributes(
                    amount = amount,
                    description = description,
                    remarks = remarks
                )
            )
        )
    )
}