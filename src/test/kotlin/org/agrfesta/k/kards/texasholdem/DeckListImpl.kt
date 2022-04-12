package org.agrfesta.k.kards.texasholdem

import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.cards.playingcards.deck.Deck

class DeckListImpl(list: List<Card>): Deck {
    private val list: MutableList<Card> = list.toMutableList()

    override fun draw(): Card = list.removeAt(0)
    override fun isEmpty(): Boolean = list.isEmpty()
    override fun size(): Int = list.size
}
