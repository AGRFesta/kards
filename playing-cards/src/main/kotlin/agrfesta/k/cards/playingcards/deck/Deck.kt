package agrfesta.k.cards.playingcards.deck

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.utils.ShufflingService
import agrfesta.k.cards.playingcards.utils.SimpleShufflingService

interface Deck {
    fun draw(num: Int): List<Card> = (0 until num).map { draw() }
    fun draw(): Card
    fun isEmpty(): Boolean
    fun size(): Int
}

class DeckImpl(private val cards: MutableList<Card> = mutableListOf()) : Deck {

    override fun draw(): Card {
        if (cards.isEmpty()) {
            throw IllegalStateException("Trying to draw a card from an empty deck")
        }
        return cards.removeAt(0)
    }

    override fun isEmpty() = cards.isEmpty()
    override fun size() = cards.size

}

//TODO test
class DeckBuilder {
    private var shuffler: ShufflingService = SimpleShufflingService()

    fun shuffleWith(shuffler: ShufflingService): DeckBuilder {
        this.shuffler = shuffler
        return this
    }

    fun build(cards: Collection<Card>): Deck {
        val list = cards.toMutableList()
        shuffler.shuffle(list)
        return DeckImpl(list)
    }
    fun build(vararg cards: Card): Deck {
        val list = cards.toMutableList()
        shuffler.shuffle(list)
        return DeckImpl(list)
    }

}