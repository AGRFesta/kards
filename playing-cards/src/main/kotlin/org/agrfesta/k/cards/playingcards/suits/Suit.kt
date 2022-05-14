package org.agrfesta.k.cards.playingcards.suits

import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.cards.playingcards.cards.cardOf
import org.agrfesta.k.cards.playingcards.deck.Deck
import org.agrfesta.k.cards.playingcards.deck.buildDeck
import org.agrfesta.k.cards.playingcards.utils.simpleShuffler
import org.agrfesta.k.cards.playingcards.utils.simpleShufflerWith
import kotlin.random.Random

/**
 * Enum representing all playing card suits
 */
enum class Suit(val cards: Set<Card>) {
    FRENCH(frenchCards()),
    ITALIAN(italianCards());

    /**
     * Creates and returns a complete [Deck] of the specific [Suit].
     * It is possible to specify a [Random] to obtain a predictable shuffled sequence of cards.
     */
    fun createDeck(random: Random? = null): Deck = buildDeck {
            withCards(cards)
            shuffleWith(random?.let { simpleShufflerWith(it) } ?: simpleShuffler)
        }

}

private fun frenchCards(): Set<Card> {
    val allCards = HashSet<Card>()
    for (s in FrenchSeed.values()) {
        for (v in frenchRanksSet) {
            allCards.add(cardOf(v, s))
        }
    }
    return allCards
}

private fun italianCards(): Set<Card> {
    val allCards = HashSet<Card>()
    for (s in ItalianSeed.values()) {
        for (v in italianRanksSet) {
            allCards.add(cardOf(v, s))
        }
    }
    return allCards
}
