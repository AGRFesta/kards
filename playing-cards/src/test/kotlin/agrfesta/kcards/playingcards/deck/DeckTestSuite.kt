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
    fun testingDeck(): Deck

    @Test
    @DisplayName("Draw a card from an empty deck -> throws EmptyDeckException")
    fun drawACardFromEmptyDeckThrowsException() {
        val deck = testingDeck()
        assertThat(deck.size()).isEqualTo(0)

        assertThat {
            deck.draw()
        }.isFailure()
                //TODO .hasClass(EmptyDeckException)
                .hasMessage("Trying to draw a card from an empty deck")
    }
    @Test
    @DisplayName("Draw two cards from a one card deck -> throws EmptyDeckException")
    fun drawTwoCardsFromOneCardDeckThrowsException() {
        val deck = testingDeck()
        deck.add(aCard())
        assertThat(deck.size()).isEqualTo(1)

        assertThat {
            deck.draw(2)
        }.isFailure()
                //TODO .hasClass(EmptyDeckException)
                .hasMessage("Trying to draw a card from an empty deck")
    }

    @Test
    @DisplayName("Add three cards to an empty deck engine -> draw those cards")
    fun addThreeCardAndDrawThoseCards() {
        val deck = testingDeck()
        assertThat(deck.size()).isEqualTo(0)

        // Add first card
        val cardA = cardOf(rankOf('A',1), aSeed())
        deck.add(cardA)
        assertThat(deck.size()).isEqualTo(1)

        // Add second card
        val cardB = cardOf(rankOf('B',1), aSeed())
        deck.add(cardB)
        assertThat(deck.size()).isEqualTo(2)

        // Add third card
        val cardC = cardOf(rankOf('C',1), aSeed())
        deck.add(cardC)
        assertThat(deck.size()).isEqualTo(3)

        assertThat(deckContent(deck)).containsOnly(cardA,cardC,cardB)
        assertThat(deck.size()).isEqualTo(0)
    }
    @Test
    @DisplayName("Add three cards at once to an empty deck engine -> draw those cards")
    fun addThreeCardAtOnceAndDrawThoseCards() {
        val deck = testingDeck()
        assertThat(deck.size()).isEqualTo(0)

        val cardA = cardOf(rankOf('A',1), aSeed())
        val cardB = cardOf(rankOf('B',1), aSeed())
        val cardC = cardOf(rankOf('C',1), aSeed())
        val cards = listOf(cardA, cardB, cardC)
        deck.add(cards)
        assertThat(deck.size()).isEqualTo(3)

        assertThat(deckContent(deck)).containsOnly(cardA,cardC,cardB)
        assertThat(deck.size()).isEqualTo(0)
    }
}