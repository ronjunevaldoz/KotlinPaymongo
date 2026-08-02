package com.ronjune.valdoz.kpaymongo

import io.github.ronjunevaldoz.paymongo.models.resource.CreateCustomerInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreatePaymentLinkInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreateRefundInput
import io.github.ronjunevaldoz.paymongo.models.resource.CustomerResponse
import io.github.ronjunevaldoz.paymongo.models.resource.CustomersResponse
import io.github.ronjunevaldoz.paymongo.models.resource.DeletedCustomerResponse
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentLinkPaymentsResponse
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentLinkResponse
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentLinksResponse
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentsResponse
import io.github.ronjunevaldoz.paymongo.models.resource.RefundResponse
import io.github.ronjunevaldoz.paymongo.models.resource.UpdateCustomerInput
import io.github.ronjunevaldoz.paymongo.serialization.PayMongoJson
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NewResourcesTest {
    @Test
    fun `Create payment link input should serialize amount and currency`() {
        val input = CreatePaymentLinkInput(
            amount = 10000,
            currency = "PHP",
            description = "Test",
            remarks = "note"
        )
        val json = PayMongoJson.encodeToString(CreatePaymentLinkInput.serializer(), input)
        val decoded = PayMongoJson.decodeFromString(CreatePaymentLinkInput.serializer(), json)
        assertEquals(10000, decoded.amount)
        assertEquals("PHP", decoded.currency)
    }

    // Captured from a live sandbox POST /v1/payment_links call
    @Test
    fun `Payment link response should not throw an exception`() {
        val json = """
            {
              "data": {
                "id": "link_300754c52a8761ebe30dfdc8",
                "amount": 10000,
                "currency": "PHP",
                "description": "claude audit test link",
                "livemode": false,
                "status": "active",
                "url": "https://pm.link/org-abc/test/gM3hd11",
                "reference_number": "gM3hd11",
                "metadata": {},
                "restrictions": {
                  "completed_sessions": {
                    "count": 0,
                    "limit": 1
                  }
                },
                "created_at": "2026-08-02T23:05:37Z",
                "updated_at": "2026-08-02T23:05:37Z"
              }
            }
        """.trimIndent()
        val response = PayMongoJson.decodeFromString(PaymentLinkResponse.serializer(), json)
        assertEquals("active", response.data.status)
        assertEquals(1, response.data.restrictions?.completedSessions?.limit)
    }

    // Captured from a live sandbox GET /v1/payment_links call
    @Test
    fun `Payment links list response should not throw an exception`() {
        val json = """
            {
              "data": [
                {
                  "id": "link_xkZBPitmewE1YABBuMN8b5jh",
                  "amount": 135000,
                  "currency": "PHP",
                  "description": null,
                  "remarks": null,
                  "status": "active",
                  "livemode": false,
                  "url": "https://pm.link/org-abc/test/d677VxY",
                  "reference_number": "d677VxY",
                  "metadata": {},
                  "restrictions": null,
                  "created_at": "2024-10-05T11:40:17Z",
                  "updated_at": "2024-10-05T11:40:17Z"
                }
              ]
            }
        """.trimIndent()
        val response = PayMongoJson.decodeFromString(PaymentLinksResponse.serializer(), json)
        assertEquals(1, response.data.size)
        assertFalse(response.hasMore)
    }

    // Captured from a live sandbox GET /v1/payment_links/{id}/payments call
    @Test
    fun `Payment link payments response should not throw an exception`() {
        val json = """
            {
              "data": [
                {
                  "payment_id": "pay_KXVd5SLmqF99kNFKrSTbiL6o",
                  "amount": 135000,
                  "currency": "PHP",
                  "livemode": false,
                  "description": "PREMIUM subscription for 3 month/s",
                  "status": "paid",
                  "created_at": "2024-10-05T11:41:35Z",
                  "updated_at": "2024-10-05T11:41:35Z"
                }
              ]
            }
        """.trimIndent()
        val response = PayMongoJson.decodeFromString(PaymentLinkPaymentsResponse.serializer(), json)
        assertEquals(1, response.data.size)
        assertEquals("paid", response.data[0].status)
    }

    // Request shape verified against a live sandbox POST /v1/payment_links/{id}/refunds call
    // (flat body -- no data/attributes wrapping, unlike the older Customer/Payment resources)
    @Test
    fun `Create refund input should serialize amount and payment id`() {
        val input = CreateRefundInput(
            amount = 100.0,
            paymentId = "pay_123",
            reason = "requested_by_customer"
        )
        val json = PayMongoJson.encodeToString(CreateRefundInput.serializer(), input)
        val decoded = PayMongoJson.decodeFromString(CreateRefundInput.serializer(), json)
        assertEquals("pay_123", decoded.paymentId)
        assertEquals("requested_by_customer", decoded.reason)
    }

    @Test
    fun `Refund response should not throw an exception`() {
        val json = """
            {
              "data": {
                "id": "ref_abc123",
                "amount": 10000,
                "currency": "PHP",
                "status": "succeeded",
                "payment_id": "pay_123",
                "reason": "requested_by_customer",
                "livemode": false,
                "created_at": "2026-08-02T23:05:37Z",
                "updated_at": "2026-08-02T23:05:37Z"
              }
            }
        """.trimIndent()
        val response = PayMongoJson.decodeFromString(RefundResponse.serializer(), json)
        assertEquals("succeeded", response.data.status)
    }

    @Test
    fun `Create customer input should serialize required fields`() {
        val input = CreateCustomerInput(
            data = CreateCustomerInput.CustomerInput(
                attributes = CreateCustomerInput.Attributes(
                    firstName = "Ron",
                    lastName = "Valdoz",
                    email = "ronjune.lopez@gmail.com",
                    defaultDevice = "phone",
                    phone = "+639066835308"
                )
            )
        )
        val json = PayMongoJson.encodeToString(CreateCustomerInput.serializer(), input)
        val decoded = PayMongoJson.decodeFromString(CreateCustomerInput.serializer(), json)
        assertEquals("ronjune.lopez@gmail.com", decoded.data.attributes.email)
    }

    @Test
    fun `Update customer input should allow partial fields`() {
        val input = UpdateCustomerInput(
            data = UpdateCustomerInput.CustomerInput(
                attributes = UpdateCustomerInput.Attributes(phone = "+639066835308")
            )
        )
        val json = PayMongoJson.encodeToString(UpdateCustomerInput.serializer(), input)
        val decoded = PayMongoJson.decodeFromString(UpdateCustomerInput.serializer(), json)
        assertEquals("+639066835308", decoded.data.attributes.phone)
        assertEquals(null, decoded.data.attributes.email)
    }

    @Test
    fun `Customer response should not throw an exception`() {
        val json = """
            {
              "data": {
                "id": "cus_abc123",
                "type": "customer",
                "attributes": {
                  "first_name": "Ron",
                  "last_name": "Valdoz",
                  "email": "ronjune.lopez@gmail.com",
                  "phone": "+639066835308",
                  "default_device": "phone",
                  "livemode": false,
                  "created_at": 1728128417,
                  "updated_at": 1728128417
                }
              }
            }
        """.trimIndent()
        val response = PayMongoJson.decodeFromString(CustomerResponse.serializer(), json)
        assertEquals("cus_abc123", response.data.id)
    }

    @Test
    fun `Customers list response should not throw an exception`() {
        val json = """
            {
              "data": [
                {
                  "id": "cus_abc123",
                  "type": "customer",
                  "attributes": {
                    "first_name": "Ron",
                    "last_name": "Valdoz",
                    "email": "ronjune.lopez@gmail.com",
                    "phone": null,
                    "default_device": "phone",
                    "livemode": false,
                    "created_at": 1728128417,
                    "updated_at": 1728128417
                  }
                }
              ]
            }
        """.trimIndent()
        val response = PayMongoJson.decodeFromString(CustomersResponse.serializer(), json)
        assertEquals(1, response.data.size)
    }

    @Test
    fun `Deleted customer response should not throw an exception`() {
        val json = """
            {
              "data": {
                "type": "deleted_entity",
                "id": "cus_abc123",
                "attributes": {
                  "deleted": true
                }
              }
            }
        """.trimIndent()
        val response = PayMongoJson.decodeFromString(DeletedCustomerResponse.serializer(), json)
        assertTrue(response.data.attributes.deleted)
    }

    @Test
    fun `Payments list response should not throw an exception`() {
        val json = """
            {
              "data": [
                {
                  "id": "pay_D4x2k6tGZ4SsDfiWkhLHeFPQ",
                  "type": "payment",
                  "attributes": {
                    "access_url": null,
                    "amount": 150000,
                    "balance_transaction_id": "bal_txn_XkAGzTuQoxMJaXXXif5oNA66",
                    "billing": {
                      "address": {
                        "city": "",
                        "country": "",
                        "line1": "",
                        "line2": "",
                        "postal_code": "",
                        "state": ""
                      },
                      "email": "ronjune.lopez@gmail.com",
                      "name": "Ron June Valdoz",
                      "phone": "+639066835308"
                    },
                    "currency": "PHP",
                    "description": null,
                    "disputed": false,
                    "external_reference_number": null,
                    "fee": 3750,
                    "livemode": false,
                    "net_amount": 146250,
                    "origin": "api",
                    "payment_intent_id": null,
                    "payout": null,
                    "source": {
                      "id": "src_RdsqYgNn89FjUEHT4frfHg6X",
                      "type": "gcash"
                    },
                    "statement_descriptor": "PAYMONGO",
                    "status": "paid",
                    "tax_amount": null,
                    "metadata": null,
                    "refunds": [],
                    "taxes": [],
                    "available_at": 1679562000,
                    "created_at": 1679383287,
                    "credited_at": 1680080400,
                    "paid_at": 1679383287
                  }
                }
              ],
              "has_more": false
            }
        """.trimIndent()
        val response = PayMongoJson.decodeFromString(PaymentsResponse.serializer(), json)
        assertEquals(1, response.data.size)
        assertFalse(response.hasMore)
    }
}
