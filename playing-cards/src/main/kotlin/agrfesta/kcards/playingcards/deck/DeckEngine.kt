package agrfesta.kcards.playingcards.deck

import agrfesta.kcards.playingcards.cards.Card
import java.util.*

interface DeckEngine {
    fun size(): Int
    fun add(card: Card)
    fun add(cards: Collection<Card>)
    fun draw(): Optional<Card>
}