package io.github.ronjunevaldoz.paymongo.serialization

import io.github.ronjunevaldoz.paymongo.models.resource.Payment
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentIntent
import io.github.ronjunevaldoz.paymongo.models.resource.Link
import io.github.ronjunevaldoz.paymongo.models.resource.Resource
import io.github.ronjunevaldoz.paymongo.models.resource.Source
import io.github.ronjunevaldoz.paymongo.models.resource.Webhook
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val ResourceModule = SerializersModule {
    polymorphic(Resource::class) {
        subclass(Link::class, Link.serializer())
        subclass(Source::class, Source.serializer())
        subclass(Payment::class, Payment.serializer())
        subclass(PaymentIntent::class, PaymentIntent.serializer())
        subclass(Webhook::class, Webhook.serializer())
    }
}