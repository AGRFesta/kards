package agrfesta.kcards.playingcards.deck

import agrfesta.kcards.playingcards.cards.Card
import java.util.*


class DeckShuffleEngine : DeckEngine {
    private val cards = Stack<Card>()

    override fun size() = cards.size

    override fun add(card: Card) {
        cards.add(card)
        cards.shuffle()
    }

    override fun add(cards: Collection<Card>) {
        this.cards.addAll(cards)
        this.cards.shuffle()
    }

    override fun draw() =
        if (cards.isEmpty()) {
            Optional.empty()
        } else {
            Optional.of(cards.pop())
        }

}
