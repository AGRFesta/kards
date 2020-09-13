package agrfesta.k.cards.playingcards.suits

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.cards.cardOf
import agrfesta.k.cards.playingcards.deck.Deck
import agrfesta.k.cards.playingcards.deck.DeckBuilder
import agrfesta.k.cards.playingcards.utils.SimpleShufflingService

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
            DeckBuilder(SimpleShufflingService())
                .build(cards)

}

private fun frenchCards(): Set<Card> {
    val allCards = HashSet<Card>()
    for (s in FrenchSeed.values()) {
        for (v in FrenchRank.values()) {
            allCards.add(cardOf(v.adapter, s))
        }
    }
    return allCards
}

private fun italianCards(): Set<Card> {
    val allCards = HashSet<Card>()
    for (s in ItalianSeed.values()) {
        for (v in ItalianRank.values()) {
            allCards.add(cardOf(v.adapter, s))
        }
    }
    return allCards
}
