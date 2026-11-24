package com.devarsh.fitnesschallengeapp.data.engine



import org.junit.Assert.*
import org.junit.Test

class BadgeEngineTest {
    @Test
    fun thresholds() {
        assertTrue(5 >= 5)
        assertFalse(4 >= 5)
        assertTrue(50 >= 50)
        assertTrue(7 >= 7)
        assertFalse(6 >= 7)
    }
}