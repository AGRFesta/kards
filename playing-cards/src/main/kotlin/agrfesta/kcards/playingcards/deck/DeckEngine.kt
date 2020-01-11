package agrfesta.kcards.playingcards.deck

import agrfesta.kcards.playingcards.cards.Card

interface DeckEngine {
    fun size(): Int
    fun add(vararg cards: Card)
    fun add(cards: Collection<Card>)
    fun draw(): Card
}