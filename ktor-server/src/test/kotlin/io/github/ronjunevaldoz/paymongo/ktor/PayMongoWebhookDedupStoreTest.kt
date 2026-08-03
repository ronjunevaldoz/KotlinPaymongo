package io.github.ronjunevaldoz.paymongo.ktor

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PayMongoWebhookDedupStoreTest {
    @Test
    fun `first sighting of an event id is not a duplicate`() = runBlocking {
        val store = InMemoryPayMongoWebhookDedupStore()
        assertTrue(store.markSeen("evt_1"))
    }

    @Test
    fun `repeat sighting of the same event id is a duplicate`() = runBlocking {
        val store = InMemoryPayMongoWebhookDedupStore()
        store.markSeen("evt_1")
        assertFalse(store.markSeen("evt_1"))
        assertFalse(store.markSeen("evt_1"))
    }

    @Test
    fun `different event ids are independent`() = runBlocking {
        val store = InMemoryPayMongoWebhookDedupStore()
        assertTrue(store.markSeen("evt_1"))
        assertTrue(store.markSeen("evt_2"))
    }
}
