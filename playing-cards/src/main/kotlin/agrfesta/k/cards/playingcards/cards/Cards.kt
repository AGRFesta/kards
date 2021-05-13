package agrfesta.k.cards.playingcards.cards

/**
 * Represents a playing card, with rank and seed.
 */
interface Card {
    val rank: Rank
    val seed: Seed
}

data class CardImpl(override val rank: Rank, override val seed: Seed): Card {
    override fun toString(): String = "${rank.symbol}${seed.symbol}"
}

/**
 * Create and returns a new [Card] with [rank] and [seed].
 */
fun cardOf(rank: Rank, seed: Seed) = CardImpl(rank, seed)

/**
 * Represents a playing card rank, usually is a number or a figure.
 */
interface Rank: Comparable<Rank> {
    val symbol: Char
    val ordinal: Int

    /**
     * Returns the [Rank] in actual ordinal position plus [increment].
     */
    operator fun plus(increment: Int): Rank

    /**
     * Returns the [Rank] in actual ordinal position plus [decrement].
     */
    operator fun minus(decrement: Int): Rank
}

/**
 * Represents a playing card seed, usually is a symbol.
 */
interface Seed {
    val symbol: Char
    val ordinal: Int
}
