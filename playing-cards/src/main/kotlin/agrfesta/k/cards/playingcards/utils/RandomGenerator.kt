package agrfesta.k.cards.playingcards.utils

/**
 * Abstraction of a random generator
 */
interface RandomGenerator {

    /**
     * Returns a random int value between 0 (inclusive) and the specified [bound] value (exclusive)
     */
    fun nextInt(bound: Int): Int

}
