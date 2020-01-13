package agrfesta.kcards.playingcards.deck

import agrfesta.kcards.playingcards.cards.Card

class RandomDrawDeck : Deck {
    private val cards = ArrayList<Card>()

    override fun draw(): Card {
        if (cards.isEmpty()) {
            throw EmptyDeckException()
        }
        return cards.removeAt(0)
    }

    override fun isEmpty() = cards.isEmpty()
    override fun size() = cards.size

    override fun add(vararg cards: Card) {
        this.cards.addAll(cards)
    }
    override fun add(cards: Collection<Card>) {
        this.cards.addAll(cards)
    }

}