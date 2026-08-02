package com.ronjune.valdoz.kpaymongo

import io.github.ronjunevaldoz.paymongo.models.resource.CreateCustomerInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreatePaymentLinkInput
import io.github.ronjunevaldoz.paymongo.models.resource.CreateRefundInput
import io.github.ronjunevaldoz.paymongo.models.resource.CustomerResponse
import io.github.ronjunevaldoz.paymongo.models.resource.CustomersResponse
import io.github.ronjunevaldoz.paymongo.models.resource.DeletedCustomerResponse
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
            data = CreatePaymentLinkInput.PaymentLinkInput(
                attributes = CreatePaymentLinkInput.Attributes(
                    amount = 10000,
                    currency = "PHP",
                    description = "Test",
                    remarks = "note"
                )
            )
        )
        val json = PayMongoJson.encodeToString(CreatePaymentLinkInput.serializer(), input)
        val decoded = PayMongoJson.decodeFromString(CreatePaymentLinkInput.serializer(), json)
        assertEquals(10000, decoded.data.attributes.amount)
        assertEquals("PHP", decoded.data.attributes.currency)
    }

    @Test
    fun `Payment link response should not throw an exception`() {
        val json = """
            {
              "data": {
                "id": "link_xkZBPitmewE1YABBuMN8b5jh",
                "type": "payment_link",
                "attributes": {
                  "amount": 10000,
                  "currency": "PHP",
                  "description": "Test",
                  "remarks": "note",
                  "status": "active",
                  "livemode": false,
                  "url": "https://pm.link/org-abc/test/d677VxY",
                  "reference_number": "d677VxY",
                  "metadata": null,
                  "restrictions": {
                    "completed_sessions": {
                      "count": 0,
                      "limit": 1
                    }
                  },
                  "created_at": 1728128417,
                  "updated_at": 1728128417
                }
              }
            }
        """.trimIndent()
        val response = PayMongoJson.decodeFromString(PaymentLinkResponse.serializer(), json)
        assertEquals("active", response.data.attributes.status)
        assertEquals(1, response.data.attributes.restrictions?.completedSessions?.limit)
    }

    @Test
    fun `Payment links list response should not throw an exception`() {
        val json = """
            {
              "data": [
                {
                  "id": "link_xkZBPitmewE1YABBuMN8b5jh",
                  "type": "payment_link",
                  "attributes": {
                    "amount": 10000,
                    "currency": "PHP",
                    "description": null,
                    "remarks": null,
                    "status": "active",
                    "livemode": false,
                    "url": "https://pm.link/org-abc/test/d677VxY",
                    "reference_number": "d677VxY",
                    "metadata": null,
                    "restrictions": null,
                    "created_at": 1728128417,
                    "updated_at": 1728128417
                  }
                }
              ],
              "has_more": false
            }
        """.trimIndent()
        val response = PayMongoJson.decodeFromString(PaymentLinksResponse.serializer(), json)
        assertEquals(1, response.data.size)
        assertFalse(response.hasMore)
    }

    @Test
    fun `Create refund input should serialize amount and payment id`() {
        val input = CreateRefundInput(
            data = CreateRefundInput.RefundInput(
                attributes = CreateRefundInput.Attributes(
                    amount = 100.0,
                    paymentId = "pay_123",
                    reason = "requested_by_customer"
                )
            )
        )
        val json = PayMongoJson.encodeToString(CreateRefundInput.serializer(), input)
        val decoded = PayMongoJson.decodeFromString(CreateRefundInput.serializer(), json)
        assertEquals("pay_123", decoded.data.attributes.paymentId)
        assertEquals("requested_by_customer", decoded.data.attributes.reason)
    }

    @Test
    fun `Refund response should not throw an exception`() {
        val json = """
            {
              "data": {
                "id": "ref_abc123",
                "type": "refund",
                "attributes": {
                  "amount": 10000,
                  "currency": "PHP",
                  "status": "succeeded",
                  "payment_id": "pay_123",
                  "reason": "requested_by_customer",
                  "livemode": false,
                  "created_at": 1728128417,
                  "updated_at": 1728128417
                }
              }
            }
        """.trimIndent()
        val response = PayMongoJson.decodeFromString(RefundResponse.serializer(), json)
        assertEquals("succeeded", response.data.attributes.status)
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
