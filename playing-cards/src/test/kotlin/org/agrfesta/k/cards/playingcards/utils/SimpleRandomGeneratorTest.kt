package org.agrfesta.k.cards.playingcards.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThanOrEqualTo
import assertk.assertions.isLessThan
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.random.Random

@DisplayName("Simple random generator Tests")
class SimpleRandomGeneratorTest {

    @Test
    @DisplayName("if bound is 3 -> nextInt returns always an int in range [0,3)")
    fun testNextIntResultRange() {
        val generator = SimpleRandomGenerator()

        val num = generator.nextInt(3)

        assertThat(num).isGreaterThanOrEqualTo(0)
        assertThat(num).isLessThan(3)
    }

    @Test
    @DisplayName("constructor(): two generators with same random -> returns same sequence")
    fun constructor_twoGeneratorsWithSameRandom_returnsSameSequence() {
        val seed = 290580L
        val generatorA = SimpleRandomGenerator(Random(seed))
        val generatorB = SimpleRandomGenerator(Random(seed))

        val sequenceA = (0 .. 10).map { generatorA.nextInt(5) }
        val sequenceB = (0 .. 10).map { generatorB.nextInt(5) }

        assertThat(sequenceA).isEqualTo(sequenceB)
    }

}
