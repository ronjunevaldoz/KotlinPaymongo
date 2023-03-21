package com.ronjune.valdoz.kpaymongo

import com.ronjunevaldoz.kpaymongo.AppJson
import com.ronjunevaldoz.kpaymongo.models.resource.ReceiveWebhookEvent
import kotlinx.serialization.decodeFromString
import kotlin.test.Test

class ReceivedWebhookEventTest {
    @Test
    fun `Test paid payment event json decode`() {
        val paidPaymentEventJsonString = "{\n" +
                "    \"data\": {\n" +
                "        \"id\": \"evt_wHb8c2V8TD6VWVAx3HctprNU\",\n" +
                "        \"type\": \"event\",\n" +
                "        \"attributes\": {\n" +
                "            \"type\": \"payment.paid\",\n" +
                "            \"livemode\": false,\n" +
                "            \"data\": {\n" +
                "                \"id\": \"pay_D4x2k6tGZ4SsDfiWkhLHeFPQ\",\n" +
                "                \"type\": \"payment\",\n" +
                "                \"attributes\": {\n" +
                "                    \"access_url\": null,\n" +
                "                    \"amount\": 150000,\n" +
                "                    \"balance_transaction_id\": \"bal_txn_XkAGzTuQoxMJaXXXif5oNA66\",\n" +
                "                    \"billing\": {\n" +
                "                        \"address\": {\n" +
                "                            \"city\": \"\",\n" +
                "                            \"country\": \"\",\n" +
                "                            \"line1\": \"\",\n" +
                "                            \"line2\": \"\",\n" +
                "                            \"postal_code\": \"\",\n" +
                "                            \"state\": \"\"\n" +
                "                        },\n" +
                "                        \"email\": \"ronjune.lopez@gmail.com\",\n" +
                "                        \"name\": \"Ron June Valdoz\",\n" +
                "                        \"phone\": \"+639066835308\"\n" +
                "                    },\n" +
                "                    \"currency\": \"PHP\",\n" +
                "                    \"description\": null,\n" +
                "                    \"disputed\": false,\n" +
                "                    \"external_reference_number\": null,\n" +
                "                    \"fee\": 3750,\n" +
                "                    \"livemode\": false,\n" +
                "                    \"net_amount\": 146250,\n" +
                "                    \"origin\": \"api\",\n" +
                "                    \"payment_intent_id\": null,\n" +
                "                    \"payout\": null,\n" +
                "                    \"source\": {\n" +
                "                        \"id\": \"src_RdsqYgNn89FjUEHT4frfHg6X\",\n" +
                "                        \"type\": \"gcash\"\n" +
                "                    },\n" +
                "                    \"statement_descriptor\": \"PAYMONGO\",\n" +
                "                    \"status\": \"paid\",\n" +
                "                    \"tax_amount\": null,\n" +
                "                    \"metadata\": null,\n" +
                "                    \"refunds\": [],\n" +
                "                    \"taxes\": [],\n" +
                "                    \"available_at\": 1679562000,\n" +
                "                    \"created_at\": 1679383287,\n" +
                "                    \"credited_at\": 1680080400,\n" +
                "                    \"paid_at\": 1679383287\n" +
                "                }\n" +
                "            },\n" +
                "            \"previous_data\": {},\n" +
                "            \"created_at\": 1647501632,\n" +
                "            \"updated_at\": 1647501632\n" +
                "        }\n" +
                "    }\n" +
                "}"
        try {
            AppJson.decodeFromString<ReceiveWebhookEvent>(paidPaymentEventJsonString)
            assert(true)
        } catch (e: Exception) {
            assert(false)
        }
    }

    @Test
    fun `Test source json decode`() {
        val sourceEventJsonString = "{\n" +
                "    \"data\": {\n" +
                "        \"id\": \"evt_wHb8c2V8TD6VWVAx3HctprNU\",\n" +
                "        \"type\": \"event\",\n" +
                "        \"attributes\": {\n" +
                "            \"type\": \"source.chargeable\",\n" +
                "            \"livemode\": false,\n" +
                "            \"data\": {\n" +
                "                \"id\": \"src_ijNqd8RGY1yNSNVTGoQsUfM6\",\n" +
                "                \"type\": \"source\",\n" +
                "                \"attributes\": {\n" +
                "                    \"amount\": 10000,\n" +
                "                    \"billing\": {\n" +
                "                        \"address\": {\n" +
                "                            \"city\": \"\",\n" +
                "                            \"country\": \"\",\n" +
                "                            \"line1\": \"\",\n" +
                "                            \"line2\": \"\",\n" +
                "                            \"postal_code\": \"\",\n" +
                "                            \"state\": \"\"\n" +
                "                        },\n" +
                "                        \"email\": \"golearn@yopmail.com\",\n" +
                "                        \"name\": \"Ron val\",\n" +
                "                        \"phone\": \"123422\"\n" +
                "                    },\n" +
                "                    \"currency\": \"PHP\",\n" +
                "                    \"description\": null,\n" +
                "                    \"livemode\": false,\n" +
                "                    \"redirect\": {\n" +
                "                        \"checkout_url\": \"https://test-sources.paymongo.com/sources?id=src_h1K5v5gXbzSwUk2VzU5zHDt7\",\n" +
                "                        \"failed\": \"http://localhost/failed\",\n" +
                "                        \"success\": \"http://localhost/success\"\n" +
                "                    },\n" +
                "                    \"statement_descriptor\": null,\n" +
                "                    \"status\": \"chargeable\",\n" +
                "                    \"type\": \"gcash\",\n" +
                "                    \"created_at\": 1647501615,\n" +
                "                    \"updated_at\": 1647501632\n" +
                "                }\n" +
                "            },\n" +
                "            \"previous_data\": {},\n" +
                "            \"created_at\": 1647501632,\n" +
                "            \"updated_at\": 1647501632\n" +
                "        }\n" +
                "    }\n" +
                "}"
        try {
            AppJson.decodeFromString<ReceiveWebhookEvent>(sourceEventJsonString)
            assert(true)
        } catch (e: Exception) {
            assert(false)
        }
    }
}