package agrfesta.k.cards.playingcards.utils

import agrfesta.k.cards.playingcards.cards.Card

interface ShufflingService {
    fun shuffle(cards: MutableList<Card>)
}

class SimpleShufflingService : ShufflingService {
    override fun shuffle(cards: MutableList<Card>) {
        cards.shuffle()
    }
}