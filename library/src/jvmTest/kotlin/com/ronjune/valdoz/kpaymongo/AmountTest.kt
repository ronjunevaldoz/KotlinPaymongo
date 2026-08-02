package com.ronjune.valdoz.kpaymongo

import io.github.ronjunevaldoz.paymongo.models.Amount
import io.github.ronjunevaldoz.paymongo.models.centavos
import io.github.ronjunevaldoz.paymongo.models.majorUnits
import io.github.ronjunevaldoz.paymongo.models.pesos
import io.github.ronjunevaldoz.paymongo.serialization.PayMongoJson
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AmountTest {
    @Test
    fun `major units convert to minor units`() {
        assertEquals(10000, Amount.ofMajorUnits(100.0).minorUnits)
        assertEquals(54300, Amount.ofMajorUnits(543.0).minorUnits)
        assertEquals(150, Amount.ofMajorUnits(1.5).minorUnits)
    }

    @Test
    fun `minor units convert back to major units`() {
        assertEquals(100.0, Amount(10000).majorUnits)
        assertEquals(543.0, Amount(54300).majorUnits)
        assertEquals(1.5, Amount(150).majorUnits)
    }

    @Test
    fun `rounds fractional minor units instead of truncating`() {
        // 19.999 pesos should round to 2000 centavos, not truncate to 1999
        assertEquals(2000, Amount.ofMajorUnits(19.999).minorUnits)
    }

    @Test
    fun `arithmetic and comparison operate on minor units`() {
        val a = Amount(10000)
        val b = Amount(500)
        assertEquals(Amount(10500), a + b)
        assertEquals(Amount(9500), a - b)
        assertTrue(a > b)
    }

    @Test
    fun `centavos is an alias for minorUnits`() {
        assertEquals(10000, Amount(10000).centavos)
        assertEquals(Amount(10000).minorUnits, Amount(10000).centavos)
    }

    @Test
    fun `int centavos builds an Amount literal`() {
        assertEquals(Amount(1000), 1000.centavos)
        assertEquals(1000, 1000.centavos.minorUnits)
    }

    @Test
    fun `double majorUnits builds an Amount from pesos`() {
        assertEquals(Amount(10000), 100.0.majorUnits)
        assertEquals(100.0.majorUnits, 10000.centavos)
    }

    @Test
    fun `pesos is an alias for majorUnits`() {
        assertEquals(100.0, Amount(10000).pesos)
        assertEquals(Amount(10000).majorUnits, Amount(10000).pesos)
    }

    @Test
    fun `double pesos builds an Amount, aliasing majorUnits`() {
        assertEquals(Amount(10000), 100.0.pesos)
        assertEquals(100.0.majorUnits, 100.0.pesos)
    }

    @Test
    fun `serializes as a plain JSON integer, unchanged on the wire`() {
        val json = PayMongoJson.encodeToString(Amount.serializer(), Amount(10000))
        assertEquals("10000", json)
        assertEquals(Amount(10000), PayMongoJson.decodeFromString(Amount.serializer(), "10000"))
    }
}
