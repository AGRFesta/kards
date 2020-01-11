package agrfesta.kcards.playingcards.cards

interface Card {
    fun rank(): Rank
    fun seed(): Seed
}
interface Rank {
    fun symbol(): Char
    fun ord(): Int
}
interface Seed {
    fun symbol(): Char
    fun ord(): Int
}

fun cardOf(rank: Rank, seed: Seed) = object: Card {
        override fun rank() = rank
        override fun seed() = seed
    }
fun rankOf(symbol: Char, ordinal: Int) = object: Rank {
        override fun symbol() = symbol
        override fun ord() = ordinal
    }
fun seedOf(symbol: Char, ordinal: Int) = object: Seed {
        override fun symbol() = symbol
        override fun ord() = ordinal
    }
