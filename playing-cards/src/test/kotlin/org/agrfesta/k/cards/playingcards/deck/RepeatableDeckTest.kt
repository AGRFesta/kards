package org.agrfesta.k.cards.playingcards.deck

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import org.agrfesta.k.cards.playingcards.suits.Suit
import org.agrfesta.k.cards.playingcards.utils.simpleShufflerWith
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.random.Random

class RepeatableDeckTest {

    @Test
    @DisplayName("two deck shuffled with the same seed -> decks contains cards in same order")
    fun twoDeckShuffledWithTheSameSeed_decksContainsCardsInSameOrder() {
        val seed = 290580L
        val deckA = buildDeck {
            withCardsFromSuit(Suit.FRENCH)
            shuffleWith(simpleShufflerWith(Random(seed)))
        }
        val deckB = buildDeck {
            withCardsFromSuit(Suit.FRENCH)
            shuffleWith(simpleShufflerWith(Random(seed)))
        }
        val cardsFromA = deckA.draw(52)
        val cardsFromB = deckB.draw(52)

        assertThat(cardsFromA).isEqualTo(cardsFromB)
    }

    @Test
    @DisplayName("two deck shuffled with different seeds -> decks do not contains cards in same order")
    fun twoDeckShuffledWithDifferentSeeds_decksDoNotContainsCardsInSameOrder() {
        val deckA = buildDeck {
            withCardsFromSuit(Suit.FRENCH)
            shuffleWith(simpleShufflerWith(Random(290580L)))
        }
        val deckB = buildDeck {
            withCardsFromSuit(Suit.FRENCH)
            shuffleWith(simpleShufflerWith(Random(290581L)))
        }
        val cardsFromA = deckA.draw(52)
        val cardsFromB = deckB.draw(52)

        assertThat(cardsFromA).isNotEqualTo(cardsFromB)
    }

}
