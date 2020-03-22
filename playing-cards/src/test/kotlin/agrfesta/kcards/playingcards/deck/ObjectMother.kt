package agrfesta.kcards.playingcards.deck

import agrfesta.kcards.playingcards.cards.Rank
import agrfesta.kcards.playingcards.cards.Seed

fun rankOf(symbol: Char) = object: Rank {
    override fun symbol() = symbol
    override fun compareTo(other: Rank): Int = -1
}

fun seedOf(symbol: Char, ordinal: Int) = object: Seed {
    override fun symbol() = symbol
    override fun ord() = ordinal
}