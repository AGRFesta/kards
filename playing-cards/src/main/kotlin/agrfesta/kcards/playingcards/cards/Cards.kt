package agrfesta.kcards.playingcards.cards

interface Card {
    fun rank(): Rank
    fun seed(): Seed
}
interface Rank: Comparable<Rank> {
    fun symbol(): Char
}
interface Seed {
    fun symbol(): Char
    fun ord(): Int
}

//TODO move to test utils
fun cardOf(rank: Rank, seed: Seed) = object: Card {
        override fun rank() = rank
        override fun seed() = seed
    }
fun rankOf(symbol: Char) = object: Rank {
        override fun symbol() = symbol
        override fun compareTo(other: Rank): Int = -1
}
fun seedOf(symbol: Char, ordinal: Int) = object: Seed {
        override fun symbol() = symbol
        override fun ord() = ordinal
    }
