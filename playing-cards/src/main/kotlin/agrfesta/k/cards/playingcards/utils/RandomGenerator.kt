package agrfesta.k.cards.playingcards.utils

interface RandomGenerator {

    /**
     * Returns a random int value between 0 (inclusive) and the specified value (exclusive)
     */
    fun nextInt(bound: Int): Int
}
