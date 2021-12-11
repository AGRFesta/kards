package org.agrfesta.k.cards.playingcards.suits

import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.cards.playingcards.cards.cardOf
import org.agrfesta.k.cards.playingcards.deck.Deck
import org.agrfesta.k.cards.playingcards.deck.DeckBuilder
import org.agrfesta.k.cards.playingcards.utils.simpleShuffler

/**
 * Enum representing all playing card suits
 */
enum class Suit(val cards: Set<Card>) {
    FRENCH(frenchCards()),
    ITALIAN(italianCards());

    /**
     * Creates and returns a complete [Deck] of the specific [Suit].
     */
    fun createDeck(): Deck =
            DeckBuilder(simpleShuffler)
                    .withCards(cards)
                    .build()

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
