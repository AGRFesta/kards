package agrfesta.kcards.playingcards.cards

interface Card {
    fun rank(): Rank
    fun seed(): Seed
}
fun cardOf(rank: Rank, seed: Seed) = object: Card {
    override fun rank() = rank
    override fun seed() = seed
    override fun toString(): String = "${rank().symbol()}${seed().symbol()}"
}

interface Rank: Comparable<Rank> {
    fun symbol(): Char
}
interface Seed {
    fun symbol(): Char
    fun ord(): Int
}



