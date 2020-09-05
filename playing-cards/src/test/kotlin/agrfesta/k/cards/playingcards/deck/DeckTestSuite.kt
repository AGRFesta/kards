package agrfesta.k.cards.playingcards.deck

import agrfesta.k.cards.playingcards.cards.cardOf
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

interface DeckTestSuite {
    fun testingDeck(): Deck

    @Test
    @DisplayName("Draw a card from an empty deck -> throws EmptyDeckException")
    fun drawACardFromEmptyDeckThrowsException() {
        val deck = testingDeck()
        assertThat(deck.size()).isEqualTo(0)

        val failure = assertThat { deck.draw() }.isFailure()
        failure.hasClass(EmptyDeckException::class)
        failure.hasMessage("Trying to draw a card from an empty deck")
    }
    @Test
    @DisplayName("Draw two cards from a one card deck -> throws EmptyDeckException")
    fun drawTwoCardsFromOneCardDeckThrowsException() {
        val deck = testingDeck()
        deck.add(aCard())
        assertThat(deck.size()).isEqualTo(1)

        val failure = assertThat { deck.draw(2) }.isFailure()
        failure.hasClass(EmptyDeckException::class)
        failure.hasMessage("Trying to draw a card from an empty deck")
    }

    @Test
    @DisplayName("Add three cards to an empty deck engine -> draw those cards")
    fun addThreeCardAndDrawThoseCards() {
        val deck = testingDeck()
        assertThat(deck.size()).isEqualTo(0)

        // Add first card
        val cardA = cardOf(rankOf('A'), aSeed())
        deck.add(cardA)
        assertThat(deck.size()).isEqualTo(1)

        // Add second card
        val cardB = cardOf(rankOf('B'), aSeed())
        deck.add(cardB)
        assertThat(deck.size()).isEqualTo(2)

        // Add third card
        val cardC = cardOf(rankOf('C'), aSeed())
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

        val cardA = cardOf(rankOf('A'), aSeed())
        val cardB = cardOf(rankOf('B'), aSeed())
        val cardC = cardOf(rankOf('C'), aSeed())
        val cards = listOf(cardA, cardB, cardC)
        deck.add(cards)
        assertThat(deck.size()).isEqualTo(3)

        assertThat(deckContent(deck)).containsOnly(cardA,cardC,cardB)
        assertThat(deck.size()).isEqualTo(0)
    }
}