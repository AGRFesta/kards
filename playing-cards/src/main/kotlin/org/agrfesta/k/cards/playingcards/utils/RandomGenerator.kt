package org.agrfesta.k.cards.playingcards.utils

import kotlin.random.Random

/**
 * Abstraction of a random generator
 */
interface RandomGenerator {

    /**
     * Returns a random int value between 0 (inclusive) and the specified [bound] value (exclusive)
     */
    fun nextInt(bound: Int): Int

}

class SimpleRandomGenerator(private val random: Random? = null): RandomGenerator {

    override fun nextInt(bound: Int) = random?.let { nextIntInner(bound, it) } ?: nextIntInner(bound)

    private fun nextIntInner(bound: Int) = (0 until bound).shuffled().first()
    private fun nextIntInner(bound: Int, random: Random) = (0 until bound).shuffled(random).first()
}
