package agrfesta.k.cards.playingcards.deck

import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.cards.Seed

fun rankOf(symbol: Char) = object: Rank {
    override fun symbol() = symbol
    override fun ordinal(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun plus(increment: Int): Rank {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun minus(decrement: Int): Rank {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun compareTo(other: Rank): Int = -1
}

fun seedOf(symbol: Char, ordinal: Int) = object: Seed {
    override fun symbol() = symbol
    override fun ord() = ordinal
}