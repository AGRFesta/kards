package agrfesta.k.cards.texasholdem

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.deck.Deck

class DeckListImpl(list: List<Card>): Deck {//TODO check for empty list
    private val list: MutableList<Card> = list.toMutableList()

    override fun add(vararg cards: Card) { cards.forEach { list.add(it) } }
    override fun add(cards: Collection<Card>) { cards.forEach { list.add(it) } }
    override fun draw(): Card = list.removeAt(0)
    override fun isEmpty(): Boolean = list.isEmpty()
    override fun size(): Int = list.size
}

//fun List<Card>.toDeckListImpl(): Deck = DeckListImpl(this)
