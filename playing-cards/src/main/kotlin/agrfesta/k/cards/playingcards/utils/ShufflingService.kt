package agrfesta.k.cards.playingcards.utils

import agrfesta.k.cards.playingcards.cards.Card

/**
 * Abstraction of service that shuffles a [MutableList] of [Card].
 */
interface ShufflingService {
    /**
     * Shuffles the [cards] modifying the [MutableList].
     */
    fun shuffle(cards: MutableList<Card>)
}

/**
 * Simple implementation of [ShufflingService] that wraps [MutableList.shuffle].
 */
class SimpleShufflingService : ShufflingService {
    override fun shuffle(cards: MutableList<Card>) = cards.shuffle()
}
