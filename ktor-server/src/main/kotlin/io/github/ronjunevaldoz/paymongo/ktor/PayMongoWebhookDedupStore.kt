package io.github.ronjunevaldoz.paymongo.ktor

import java.util.concurrent.ConcurrentHashMap

/**
 * PayMongo retries a webhook delivery on timeout/failure, so the same event id can arrive
 * more than once. A dedup store lets the plugin flag redeliveries so a handler doesn't
 * double-apply a side effect (e.g. fulfilling an order twice).
 */
interface PayMongoWebhookDedupStore {
    /** Returns true the first time [eventId] is seen, false on every later call with the same id. */
    suspend fun markSeen(eventId: String): Boolean
}

/**
 * Default, process-local dedup store. Fine for a single-instance deployment; a multi-instance
 * deployment needs a shared store (Redis, a database table with a unique constraint on event
 * id) since this one doesn't coordinate across processes.
 *
 * ponytail: unbounded growth, no eviction -- restart the process periodically, or swap in a
 * TTL-backed store, if this runs long enough for memory to matter.
 */
class InMemoryPayMongoWebhookDedupStore : PayMongoWebhookDedupStore {
    private val seenEventIds = ConcurrentHashMap.newKeySet<String>()

    override suspend fun markSeen(eventId: String): Boolean = seenEventIds.add(eventId)
}
