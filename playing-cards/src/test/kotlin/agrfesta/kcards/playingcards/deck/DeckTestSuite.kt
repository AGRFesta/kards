package agrfesta.kcards.playingcards.deck

import agrfesta.kcards.playingcards.cards.cardOf
import agrfesta.kcards.playingcards.cards.rankOf
import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

interface DeckTestSuite {
    fun deckUnderTest(): Deck

    @Test
    @DisplayName("Draw a card from an empty deck -> throws EmptyDeckException")
    fun drawACardFromEmptyDeckThrowsException() {
        val deck = deckUnderTest()
        assertThat(deck.size()).isEqualTo(0)

        assertThat {
            deck.draw()
        }.isFailure()
                //TODO .hasClass(EmptyDeckException.class)
                .hasMessage("Trying to draw a card from an empty deck")
    }

    @Test
    @DisplayName("Add three cards to an empty deck engine -> draw those cards")
    fun addThreeCardAndDrawThoseCards() {
        val deck = deckUnderTest()
        assertThat(deck.size()).isEqualTo(0)

        // Add first card
        val rankA = rankOf('A',1)
        val cardA = cardOf(rankA, aSeed())
        deck.add(cardA)
        assertThat(deck.size()).isEqualTo(1)

        // Add second card
        val rankB = rankOf('B',1)
        val cardB = cardOf(rankB, aSeed())
        deck.add(cardB)
        assertThat(deck.size()).isEqualTo(2)

        // Add third card
        val rankC = rankOf('C',1)
        val cardC = cardOf(rankC, aSeed())
        deck.add(cardC)
        assertThat(deck.size()).isEqualTo(3)

        assertThat(deckContent(deck)).containsOnly(cardA,cardC,cardB)
    }
}