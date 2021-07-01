package agrfesta.k.cards.playingcards.deck

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.suits.Suit
import agrfesta.k.cards.playingcards.utils.Shuffler
import agrfesta.k.cards.playingcards.utils.simpleShuffler

/**
 * Represents a deck of cards. Its internal state is mutable, cards can be drew but not added.
 */
interface Deck {

    /**
     * Returns a [List] of [Card] from deck. Cards are removed from deck.
     *
     * @throws [IllegalStateException] when the deck is empty
     */
    fun draw(num: Int): List<Card> = (0 until num).map { draw() }

    /**
     * Returns a [Card] from deck. The card is removed from deck.
     *
     * @throws [IllegalStateException] when the deck is empty
     */
    fun draw(): Card

    /**
     * Returns `true` if the deck is empty.
     */
    fun isEmpty(): Boolean

    /**
     * Returns number of cards inside deck.
     */
    fun size(): Int
}

/**
 * Simple [Deck] implementation that use an internal [MutableList] and always draw the [Card] in position 0.
 */
class DeckImpl(private val cards: MutableList<Card> = mutableListOf()) : Deck {

    override fun draw(): Card {
        check(cards.isNotEmpty()) { "Trying to draw a card from an empty deck" }
        return cards.removeAt(0)
    }

    override fun isEmpty() = cards.isEmpty()
    override fun size() = cards.size

}

/**
 * Fluent [Deck] builder.
 *
 * @param [defaultShuffler] default [Shuffler] used building [Deck], can be replaced using [shuffleWith].
 */
class DeckBuilder internal constructor(defaultShuffler: Shuffler) {
    private var shuffler: Shuffler = defaultShuffler
    private var cards: MutableList<Card> = mutableListOf()

    /**
     * Chained method, can be used to set a specific [Shuffler].
     * [shuffler] is the [Shuffler] that builder will use creating the [Deck], it will override the default one.
     */
    fun shuffleWith(shuffler: Shuffler): DeckBuilder {
        this.shuffler = shuffler
        return this
    }

    /**
     * Chained method, can be used to set deck content.
     * Deck will contain cards from this [Collection] only.
     * Will override any previous cards setting.
     */
    fun withCards(cards: Collection<Card>): DeckBuilder {
        this.cards = cards.toMutableList()
        return this
    }

    /**
     * Chained method, can be used to set deck content.
     * Deck will contain cards from this [Array] only.
     * Will override any previous cards setting.
     */
    fun withCards(vararg cards: Card): DeckBuilder {
        this.cards = cards.toMutableList()
        return this
    }

    /**
     * Chained method, can be used to set deck content.
     * Deck will contain cards from [suit] only.
     * Will override any previous cards setting.
     */
    fun withCardsFromSuit(suit: Suit): DeckBuilder {
        this.cards = suit.cards.toMutableList()
        return this
    }

    /**
     * Returns a [Deck] with all and only elements of [MutableList] [cards].
     * Cards inside the deck will be shuffled before returning it.
     *
     * if [cards] is empty will create an empty [Deck]
     */
    internal fun build(): Deck {
        shuffler(cards)
        return DeckImpl(cards)
    }

}

/**
 * Build a Deck applying the provided [setup].
 */
fun buildDeck(
    setup: DeckBuilder.() -> Unit
): Deck {
    val builder = DeckBuilder(simpleShuffler)
    builder.setup()
    return builder.build()
}

