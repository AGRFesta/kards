package agrfesta.kcards.playingcards.deck

import agrfesta.kcards.playingcards.cards.Card

interface Deck {
    fun draw(num: Int): List<Card>
}