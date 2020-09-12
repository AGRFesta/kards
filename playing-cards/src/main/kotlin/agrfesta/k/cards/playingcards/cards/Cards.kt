package agrfesta.k.cards.playingcards.cards

/**
 * Represents a playing card, with rank and seed.
 */
interface Card {
    fun rank(): Rank
    fun seed(): Seed
}

/**
 * Create and returns a new [Card] with [rank] and [seed].
 */
fun cardOf(rank: Rank, seed: Seed) = object: Card {
    override fun rank() = rank
    override fun seed() = seed
    override fun toString(): String = "${rank().symbol()}${seed().symbol()}"
    override fun equals(other: Any?): Boolean {
        if (other !is Card) return false
        return rank()==other.rank() && seed()==other.seed()
    }
    override fun hashCode(): Int = seed().hashCode() * 31 + rank().hashCode()
}

/**
 * Represents a playing card rank, usually is a number or a figure.
 */
interface Rank: Comparable<Rank> {
    fun symbol(): Char
    fun ordinal(): Int

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
    fun symbol(): Char
    fun ord(): Int
}


