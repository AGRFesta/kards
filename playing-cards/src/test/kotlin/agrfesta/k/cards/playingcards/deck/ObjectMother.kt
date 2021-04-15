package agrfesta.k.cards.playingcards.deck

import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.cards.Seed

fun rankOf(symbol: Char) = object: Rank {
    override fun symbol() = symbol
    override fun ordinal(): Int = 0
    override fun plus(increment: Int): Rank = this
    override fun minus(decrement: Int): Rank = this
    override fun compareTo(other: Rank): Int = -1
    override fun toString() = symbol.toString()
}

fun seedOf(symbol: Char, ordinal: Int) = object: Seed {
    override fun symbol() = symbol
    override fun ord() = ordinal
}
