package org.agrfesta.k.cards.playingcards.deck

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import org.agrfesta.k.cards.playingcards.suits.Suit.FRENCH
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.random.Random

class RepeatableDeckTest {

    @Test
    @DisplayName("two deck shuffled with the same seed -> decks contains cards in same order")
    fun twoDeckShuffledWithTheSameSeed_decksContainsCardsInSameOrder() {
        val seed = 290580L
        val deckA = FRENCH.createDeck(Random(seed))
        val deckB = FRENCH.createDeck(Random(seed))
        val cardsFromA = deckA.draw(52)
        val cardsFromB = deckB.draw(52)

        assertThat(cardsFromA).isEqualTo(cardsFromB)
    }

    @Test
    @DisplayName("two deck shuffled with different seeds -> decks do not contains cards in same order")
    fun twoDeckShuffledWithDifferentSeeds_decksDoNotContainsCardsInSameOrder() {
        val deckA = FRENCH.createDeck(Random(290580L))
        val deckB = FRENCH.createDeck(Random(290581L))
        val cardsFromA = deckA.draw(52)
        val cardsFromB = deckB.draw(52)

        assertThat(cardsFromA).isNotEqualTo(cardsFromB)
    }

}
