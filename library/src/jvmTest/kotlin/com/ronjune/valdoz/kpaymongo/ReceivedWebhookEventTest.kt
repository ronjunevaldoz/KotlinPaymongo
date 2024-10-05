package com.ronjune.valdoz.kpaymongo

import io.github.ronjunevaldoz.paymongo.serialization.PayMongoJson
import io.github.ronjunevaldoz.paymongo.models.resource.CheckoutSessionResponse
import io.github.ronjunevaldoz.paymongo.models.resource.CreateCheckoutSessionInput
import io.github.ronjunevaldoz.paymongo.models.resource.Link
import io.github.ronjunevaldoz.paymongo.models.resource.ReceiveWebhookEvent
import kotlin.test.Test

class ReceivedWebhookEventTest {
    @Test
    fun `Payment paid should not throw an exception`() {
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
            PayMongoJson.decodeFromString<ReceiveWebhookEvent>(paidPaymentEventJsonString)
            assert(true)
        } catch (e: Exception) {
            assert(false)
        }
    }

    @Test
    fun `Payment 2 paid should not throw an exception`() {
        val paidPaymentEventJsonString = "{\n" +
                "    \"data\": {\n" +
                "        \"id\": \"evt_jq8syswGKcyBoepP7inucLV7\",\n" +
                "        \"type\": \"event\",\n" +
                "        \"attributes\": {\n" +
                "            \"type\": \"payment.paid\",\n" +
                "            \"livemode\": false,\n" +
                "            \"data\": {\n" +
                "                \"id\": \"pay_73Dma4hhk1T4tdvbKZ4tUNEh\",\n" +
                "                \"type\": \"payment\",\n" +
                "                \"attributes\": {\n" +
                "                    \"access_url\": null,\n" +
                "                    \"amount\": 150000,\n" +
                "                    \"balance_transaction_id\": \"bal_txn_FXrXTq48A1pp1wYSHLMmHoFj\",\n" +
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
                "                    \"description\": \"To get access to GoLearn's Basic for 3 Month and enroll in the BS Nursing course.\",\n" +
                "                    \"disputed\": false,\n" +
                "                    \"external_reference_number\": null,\n" +
                "                    \"fee\": 3300,\n" +
                "                    \"livemode\": false,\n" +
                "                    \"net_amount\": 146700,\n" +
                "                    \"origin\": \"api\",\n" +
                "                    \"payment_intent_id\": \"pi_RWP6m5UUvQGcgb8yP5eyiVzC\",\n" +
                "                    \"payout\": null,\n" +
                "                    \"source\": {\n" +
                "                        \"id\": \"src_RWi3h48A1MeySxJo4YhaUu5D\",\n" +
                "                        \"type\": \"grab_pay\"\n" +
                "                    },\n" +
                "                    \"statement_descriptor\": \"GoLearn\",\n" +
                "                    \"status\": \"paid\",\n" +
                "                    \"tax_amount\": null,\n" +
                "                    \"metadata\": {\n" +
                "                        \"subscriptionId\": \"644b5a088846b11cd566ae85\",\n" +
                "                        \"agent\": \"GoLearn\",\n" +
                "                        \"userId\": \"644b5a078846b11cd566ae83\"\n" +
                "                    },\n" +
                "                    \"refunds\": [],\n" +
                "                    \"taxes\": [],\n" +
                "                    \"available_at\": 1683104400,\n" +
                "                    \"created_at\": 1682660059,\n" +
                "                    \"credited_at\": 1683709200,\n" +
                "                    \"paid_at\": 1682660059,\n" +
                "                    \"updated_at\": 1682660059\n" +
                "                }\n" +
                "            },\n" +
                "            \"previous_data\": {},\n" +
                "            \"created_at\": 1682660060,\n" +
                "            \"updated_at\": 1682660060\n" +
                "        }\n" +
                "    }\n" +
                "}"
        try {
            PayMongoJson.decodeFromString<ReceiveWebhookEvent>(paidPaymentEventJsonString)
            assert(true)
        } catch (e: Exception) {
            assert(false)
        }
    }

    @Test
    fun `Source chargeable should not throw an exception`() {
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
            PayMongoJson.decodeFromString<ReceiveWebhookEvent>(sourceEventJsonString)
            assert(true)
        } catch (e: Exception) {
            assert(false)
        }
    }

    @Test
    fun `Source expired should not throw an exception`() {
        val sourceEventJsonString = "{\n" +
                "    \"data\": {\n" +
                "        \"id\": \"evt_igPJtkJaQYXkErJ7TYy1ozeo\",\n" +
                "        \"type\": \"event\",\n" +
                "        \"attributes\": {\n" +
                "            \"type\": \"source.chargeable\",\n" +
                "            \"livemode\": false,\n" +
                "            \"data\": {\n" +
                "                \"id\": \"src_fDGs9fzLVKwaL2fH3ciT1nKu\",\n" +
                "                \"type\": \"source\",\n" +
                "                \"attributes\": {\n" +
                "                    \"amount\": 150180,\n" +
                "                    \"billing\": {\n" +
                "                        \"address\": {\n" +
                "                            \"city\": \"\",\n" +
                "                            \"country\": \"\",\n" +
                "                            \"line1\": \"\",\n" +
                "                            \"line2\": \"\",\n" +
                "                            \"postal_code\": \"\",\n" +
                "                            \"state\": \"\"\n" +
                "                        },\n" +
                "                        \"email\": \"ronjune16@gmail.com\",\n" +
                "                        \"name\": \"Ron June Valdoz\",\n" +
                "                        \"phone\": \"+639066835308\"\n" +
                "                    },\n" +
                "                    \"currency\": \"PHP\",\n" +
                "                    \"description\": null,\n" +
                "                    \"livemode\": false,\n" +
                "                    \"redirect\": {\n" +
                "                        \"checkout_url\": \"https://test-sources.paymongo.com/sources?id=src_fDGs9fzLVKwaL2fH3ciT1nKu\",\n" +
                "                        \"failed\": \"http:localhost:3000/payment-failed\",\n" +
                "                        \"success\": \"http:localhost:3000/payment-success\"\n" +
                "                    },\n" +
                "                    \"statement_descriptor\": null,\n" +
                "                    \"status\": \"expired\",\n" +
                "                    \"type\": \"gcash\",\n" +
                "                    \"metadata\": null,\n" +
                "                    \"created_at\": 1679478268,\n" +
                "                    \"updated_at\": 1679478967\n" +
                "                }\n" +
                "            },\n" +
                "            \"previous_data\": {},\n" +
                "            \"created_at\": 1679444186,\n" +
                "            \"updated_at\": 1679444186\n" +
                "        }\n" +
                "    }\n" +
                "}"
        try {
            PayMongoJson.decodeFromString<ReceiveWebhookEvent>(sourceEventJsonString)
            assert(true)
        } catch (e: Exception) {
            assert(false)
        }
    }

    @Test
    fun `Checkout session response should not throw an exception`() {
        val sourceEventJsonString = "{\n" +
                "  \"data\": {\n" +
                "    \"id\": \"cs_JVvRBBmEiQ96hA8TrpFpLYwC\",\n" +
                "    \"type\": \"checkout_session\",\n" +
                "    \"attributes\": {\n" +
                "      \"billing\": {\n" +
                "        \"address\": {\n" +
                "          \"city\": null,\n" +
                "          \"country\": null,\n" +
                "          \"line1\": null,\n" +
                "          \"line2\": null,\n" +
                "          \"postal_code\": null,\n" +
                "          \"state\": null\n" +
                "        },\n" +
                "        \"email\": \"ronjune.lopez@gmail.com\",\n" +
                "        \"name\": \"Ron\",\n" +
                "        \"phone\": \"09066835308\"\n" +
                "      },\n" +
                "      \"billing_information_fields_editable\": \"enabled\",\n" +
                "      \"cancel_url\": \"http://localhost:3000/checkout/cancelled\",\n" +
                "      \"checkout_url\": \"https://checkout.paymongo.com/cs_JVvRBBmEiQ96hA8TrpFpLYwC_client_YVx49STjCD1WjKhCo72bPkCY#cGtfdGVzdF9zTEVYOXp3UkJSRlc0SEJZb2VmQmFnalI=\",\n" +
                "      \"client_key\": \"cs_JVvRBBmEiQ96hA8TrpFpLYwC_client_YVx49STjCD1WjKhCo72bPkCY\",\n" +
                "      \"customer_email\": null,\n" +
                "      \"description\": \"Payment made here will reflect to subscription\",\n" +
                "      \"line_items\": [\n" +
                "        {\n" +
                "          \"amount\": 10050,\n" +
                "          \"currency\": \"PHP\",\n" +
                "          \"description\": \"Test item\",\n" +
                "          \"images\": [],\n" +
                "          \"name\": \"Golearn Free\",\n" +
                "          \"quantity\": 1\n" +
                "        }\n" +
                "      ],\n" +
                "      \"livemode\": false,\n" +
                "      \"merchant\": null,\n" +
                "      \"payments\": [],\n" +
                "      \"payment_intent\": {\n" +
                "        \"id\": \"pi_7hxSYj1Ddw8FAAEZh6Pfsueo\",\n" +
                "        \"type\": \"payment_intent\",\n" +
                "        \"attributes\": {\n" +
                "          \"amount\": 10050,\n" +
                "          \"capture_type\": \"automatic\",\n" +
                "          \"client_key\": \"pi_7hxSYj1Ddw8FAAEZh6Pfsueo_client_LvGb6FPRWpEVBhqNiG5Cnrdn\",\n" +
                "          \"currency\": \"PHP\",\n" +
                "          \"description\": \"Payment made here will reflect to subscription\",\n" +
                "          \"livemode\": false,\n" +
                "          \"statement_descriptor\": \"GoLearn Subscription\",\n" +
                "          \"status\": \"awaiting_payment_method\",\n" +
                "          \"last_payment_error\": null,\n" +
                "          \"payment_method_allowed\": [\n" +
                "            \"gcash\",\n" +
                "            \"paymaya\",\n" +
                "            \"card\",\n" +
                "            \"grab_pay\",\n" +
                "            \"dob\",\n" +
                "            \"billease\",\n" +
                "            \"atome\"\n" +
                "          ],\n" +
                "          \"payments\": [],\n" +
                "          \"next_action\": null,\n" +
                "          \"payment_method_options\": {\n" +
                "            \"card\": {\n" +
                "              \"request_three_d_secure\": \"any\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"metadata\": null,\n" +
                "          \"setup_future_usage\": null,\n" +
                "          \"created_at\": 1682145975,\n" +
                "          \"updated_at\": 1682145975\n" +
                "        }\n" +
                "      },\n" +
                "      \"payment_method_types\": [\n" +
                "        \"card\",\n" +
                "        \"paymaya\",\n" +
                "        \"dob\",\n" +
                "        \"atome\",\n" +
                "        \"billease\",\n" +
                "        \"grab_pay\",\n" +
                "        \"gcash\"\n" +
                "      ],\n" +
                "      \"reference_number\": \"ref_123\",\n" +
                "      \"send_email_receipt\": false,\n" +
                "      \"show_description\": true,\n" +
                "      \"show_line_items\": true,\n" +
                "      \"status\": \"active\",\n" +
                "      \"success_url\": \"http://localhost:3000/checkout/success\",\n" +
                "      \"created_at\": 1682145975,\n" +
                "      \"updated_at\": 1682145975,\n" +
                "      \"metadata\": null\n" +
                "    }\n" +
                "  }\n" +
                "}"
        try {
            PayMongoJson.decodeFromString<CheckoutSessionResponse>(sourceEventJsonString)
            assert(true)
        } catch (e: Exception) {
            assert(false)
        }
    }

    @Test
    fun `Create Checkout session should not throw an exception`() {
        val sourceEventJsonString = "{\n" +
                "  \"data\": {\n" +
                "    \"attributes\": {\n" +
                "      \"billing\": {\n" +
                "        \"name\": \"Ron\",\n" +
                "        \"email\": \"ronjune.lopez@gmail.com\",\n" +
                "        \"phone\": \"09066835308\"\n" +
                "      },\n" +
                "      \"line_items\": [\n" +
                "        {\n" +
                "          \"currency\": \"PHP\",\n" +
                "          \"amount\": 10050,\n" +
                "          \"description\": \"Test item\",\n" +
                "          \"name\": \"Golearn Free\",\n" +
                "          \"quantity\": 1\n" +
                "        }\n" +
                "      ],\n" +
                "      \"payment_method_types\": [\n" +
                "        \"atome\",\n" +
                "        \"billease\",\n" +
                "        \"card\",\n" +
                "        \"dob\",\n" +
                "        \"gcash\",\n" +
                "        \"grab_pay\",\n" +
                "        \"paymaya\"\n" +
                "      ],\n" +
                "      \"send_email_receipt\": false,\n" +
                "      \"show_description\": true,\n" +
                "      \"show_line_items\": true,\n" +
                "      \"cancel_url\": \"http://localhost:3000/checkout/cancelled\",\n" +
                "      \"reference_number\": \"ref_123\",\n" +
                "      \"success_url\": \"http://localhost:3000/checkout/success\",\n" +
                "      \"statement_descriptor\": \"GoLearn Subscription\",\n" +
                "      \"description\": \"Payment made here will reflect to subscription\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        try {
            PayMongoJson.decodeFromString<CreateCheckoutSessionInput>(sourceEventJsonString)
            assert(true)
        } catch (e: Exception) {
            assert(false)
        }
    }

    @Test
    fun `Expire Checkout session should not throw an exception`() {
        val sourceEventJsonString = "{\n" +
                "  \"data\": {\n" +
                "    \"id\": \"cs_8ESmjsyoJUgJtSnfKYr94hdJ\",\n" +
                "    \"type\": \"checkout_session\",\n" +
                "    \"attributes\": {\n" +
                "      \"billing\": {\n" +
                "        \"address\": {\n" +
                "          \"city\": null,\n" +
                "          \"country\": null,\n" +
                "          \"line1\": null,\n" +
                "          \"line2\": null,\n" +
                "          \"postal_code\": null,\n" +
                "          \"state\": null\n" +
                "        },\n" +
                "        \"email\": \"ronjune.lopez@gmail.com\",\n" +
                "        \"name\": \"Ron\",\n" +
                "        \"phone\": \"09066835308\"\n" +
                "      },\n" +
                "      \"billing_information_fields_editable\": \"enabled\",\n" +
                "      \"cancel_url\": \"http://localhost:3000/checkout/cancelled\",\n" +
                "      \"checkout_url\": \"https://checkout.paymongo.com/cs_8ESmjsyoJUgJtSnfKYr94hdJ_client_UYzNWYSceT54mPrH8uBdgJVQ#cGtfdGVzdF9zTEVYOXp3UkJSRlc0SEJZb2VmQmFnalI=\",\n" +
                "      \"client_key\": \"cs_8ESmjsyoJUgJtSnfKYr94hdJ_client_UYzNWYSceT54mPrH8uBdgJVQ\",\n" +
                "      \"customer_email\": null,\n" +
                "      \"description\": \"Payment made here will reflect to subscription\",\n" +
                "      \"line_items\": [\n" +
                "        {\n" +
                "          \"amount\": 10050,\n" +
                "          \"currency\": \"PHP\",\n" +
                "          \"description\": \"Test item\",\n" +
                "          \"images\": [],\n" +
                "          \"name\": \"Golearn Free\",\n" +
                "          \"quantity\": 1\n" +
                "        }\n" +
                "      ],\n" +
                "      \"livemode\": false,\n" +
                "      \"merchant\": null,\n" +
                "      \"payments\": [],\n" +
                "      \"payment_intent\": {\n" +
                "        \"id\": \"pi_1Amzsj5KZJq8dcD8vychjHQf\",\n" +
                "        \"type\": \"payment_intent\",\n" +
                "        \"attributes\": {\n" +
                "          \"amount\": 10050,\n" +
                "          \"capture_type\": \"automatic\",\n" +
                "          \"client_key\": \"pi_1Amzsj5KZJq8dcD8vychjHQf_client_qm79cuap6YwLEQVQAAmGoHbv\",\n" +
                "          \"currency\": \"PHP\",\n" +
                "          \"description\": \"Payment made here will reflect to subscription\",\n" +
                "          \"livemode\": false,\n" +
                "          \"statement_descriptor\": \"GoLearn Subscription\",\n" +
                "          \"status\": \"cancelled\",\n" +
                "          \"last_payment_error\": null,\n" +
                "          \"payment_method_allowed\": [\n" +
                "            \"grab_pay\",\n" +
                "            \"billease\",\n" +
                "            \"gcash\",\n" +
                "            \"atome\",\n" +
                "            \"paymaya\",\n" +
                "            \"dob\",\n" +
                "            \"card\"\n" +
                "          ],\n" +
                "          \"payments\": [],\n" +
                "          \"next_action\": null,\n" +
                "          \"payment_method_options\": {\n" +
                "            \"card\": {\n" +
                "              \"request_three_d_secure\": \"any\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"metadata\": null,\n" +
                "          \"setup_future_usage\": null,\n" +
                "          \"created_at\": 1682612594,\n" +
                "          \"updated_at\": 1682612607\n" +
                "        }\n" +
                "      },\n" +
                "      \"payment_method_types\": [\n" +
                "        \"paymaya\",\n" +
                "        \"card\",\n" +
                "        \"grab_pay\",\n" +
                "        \"atome\",\n" +
                "        \"gcash\",\n" +
                "        \"dob\",\n" +
                "        \"billease\"\n" +
                "      ],\n" +
                "      \"reference_number\": \"ref_123\",\n" +
                "      \"send_email_receipt\": false,\n" +
                "      \"show_description\": true,\n" +
                "      \"show_line_items\": true,\n" +
                "      \"status\": \"expired\",\n" +
                "      \"success_url\": \"http://localhost:3000/checkout/success\",\n" +
                "      \"created_at\": 1682612594,\n" +
                "      \"updated_at\": 1682612607,\n" +
                "      \"metadata\": null\n" +
                "    }\n" +
                "  }\n" +
                "}"
        try {
            PayMongoJson.decodeFromString<CheckoutSessionResponse>(sourceEventJsonString)
            assert(true)
        } catch (e: Exception) {
            assert(false)
        }
    }

    @Test
    fun `Link payment paid should not throw an exception`() {
        val sourceEventJsonString =
            "{\"data\":{\"id\":\"evt_AFDUsU4tTaXXLZRZedLDrjuS\",\"type\":\"event\",\"attributes\":{\"type\":\"link.payment.paid\",\"livemode\":false,\"data\":{\"id\":\"link_xkZBPitmewE1YABBuMN8b5jh\",\"type\":\"link\",\"attributes\":{\"amount\":135000,\"archived\":false,\"currency\":\"PHP\",\"description\":\"PREMIUM subscription for 3 month/s \",\"livemode\":false,\"fee\":3375,\"remarks\":\"670125a0e80fbe64c638b9b2\",\"status\":\"paid\",\"tax_amount\":null,\"taxes\":[],\"checkout_url\":\"https://pm.link/org-GapS1xaVTL395KW4ucNFgpkw/test/d677VxY\",\"reference_number\":\"d677VxY\",\"created_at\":1728128417,\"updated_at\":1728128417,\"payments\":[{\"data\":{\"id\":\"pay_KXVd5SLmqF99kNFKrSTbiL6o\",\"type\":\"payment\",\"attributes\":{\"access_url\":null,\"amount\":135000,\"balance_transaction_id\":\"bal_txn_GtjZGT7etSDVtsNtdpjwAJAN\",\"billing\":{\"address\":{\"city\":\"Taguig\",\"country\":\"PH\",\"line1\":\"12th floor The Trade and Financial Tower u1206\",\"line2\":\"32nd street and 7th Avenue\",\"postal_code\":\"1630\",\"state\":\"Bonifacio Global City\"},\"email\":\"adw@Awda.com\",\"name\":\"awdawd\",\"phone\":\"w\"},\"currency\":\"PHP\",\"description\":\"PREMIUM subscription for 3 month/s \",\"disputed\":false,\"external_reference_number\":\"d677VxY\",\"fee\":3375,\"instant_settlement\":null,\"livemode\":false,\"net_amount\":131625,\"origin\":\"links\",\"payment_intent_id\":null,\"payout\":null,\"source\":{\"id\":\"src_JZH7WUoy7jVsfcUhfVDAer1o\",\"type\":\"gcash\"},\"statement_descriptor\":\"PAYMONGO\",\"status\":\"paid\",\"tax_amount\":null,\"metadata\":{\"pm_reference_number\":\"d677VxY\"},\"refunds\":[],\"taxes\":[],\"available_at\":1728464400,\"created_at\":1728128494,\"credited_at\":1729069200,\"paid_at\":1728128494,\"updated_at\":1728128494}}}]}},\"previous_data\":{},\"created_at\":1728128495,\"updated_at\":1728128495}}}"
        try {
            PayMongoJson.decodeFromString<ReceiveWebhookEvent>(sourceEventJsonString)
            assert(true)
        } catch (e: Exception) {
            e.printStackTrace()
            assert(false)
        }
    }
}