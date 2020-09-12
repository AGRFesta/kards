package agrfesta.k.cards.playingcards.cards

interface Card {
    fun rank(): Rank
    fun seed(): Seed
}
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

interface Rank: Comparable<Rank> {
    fun symbol(): Char
    fun ordinal(): Int
    operator fun plus(increment: Int): Rank
    operator fun minus(decrement: Int): Rank
}
interface Seed {
    fun symbol(): Char
    fun ord(): Int
}


