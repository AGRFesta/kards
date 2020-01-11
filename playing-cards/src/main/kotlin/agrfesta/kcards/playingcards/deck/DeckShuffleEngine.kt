package agrfesta.kcards.playingcards.deck

import agrfesta.kcards.playingcards.cards.Card
import java.util.*


class DeckShuffleEngine(private val shufflingService: ShufflingService) : DeckEngine {
    private var cards = Stack<Card>()

    override fun size() = cards.size

    override fun add(vararg cards: Card) {
        this.cards.addAll(cards)
        this.cards = shufflingService.shuffle(this.cards)
    }

    override fun add(cards: Collection<Card>) {
        this.cards.addAll(cards)
        //this.cards.shuffle()
        this.cards = shufflingService.shuffle(this.cards)
    }

    override fun draw(): Card {
        if (cards.isEmpty()) {
            throw EmptyDeckException()
        }
        return cards.pop()
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