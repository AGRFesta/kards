package agrfesta.kcards.playingcards.deck

import agrfesta.kcards.playingcards.cards.Card
import java.util.stream.Collectors
import java.util.stream.IntStream

interface Deck {
    fun draw(num: Int): List<Card> = (0 until num).map { draw() }
    fun draw(): Card
    fun isEmpty(): Boolean
    fun size(): Int
    fun add(vararg cards: Card)
    fun add(cards: Collection<Card>)
}

class EmptyDeckException(message: String = "Trying to draw a card from an empty deck") : Exception(message)