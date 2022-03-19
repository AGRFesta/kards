package org.agrfesta.k.cards.playingcards.utils

import assertk.assertThat
import assertk.assertions.isGreaterThanOrEqualTo
import assertk.assertions.isLessThan
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Simple random generator Tests")
class SimpleRandomGeneratorTest {
    private val generator = SimpleRandomGenerator()

    @Test
    @DisplayName("if bound is 3 -> nextInt returns always an int in range [0,3)")
    fun testNextIntResultRange() {
        val num = generator.nextInt(3)
        assertThat(num).isGreaterThanOrEqualTo(0)
        assertThat(num).isLessThan(3)
    }

}
