package agrfesta.k.cards.playingcards.deck

import agrfesta.k.cards.playingcards.cards.Card
import java.util.*

class AutoShufflingDeck(private val shufflingService: ShufflingService) : Deck {
    private var cards = Stack<Card>()

    override fun draw(): Card {
        if (cards.isEmpty()) {
            throw EmptyDeckException()
        }
        return cards.pop()
    }

    override fun isEmpty() = cards.isEmpty()
    override fun size() = cards.size

    override fun add(vararg cards: Card) {
        this.cards.addAll(cards)
        this.cards = shufflingService.shuffle(this.cards)
    }
    override fun add(cards: Collection<Card>) {
        this.cards.addAll(cards)
        this.cards = shufflingService.shuffle(this.cards)
    }

}

interface ShufflingService {
    fun shuffle(cards: Stack<Card>): Stack<Card>
}

class SimpleStackShufflingService : ShufflingService {
    override fun shuffle(cards: Stack<Card>): Stack<Card> {
        cards.shuffle()
        return cards
    }
}