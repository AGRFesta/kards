package org.agrfesta.k.cards.playingcards.deck

import org.agrfesta.k.cards.playingcards.cards.cardOf
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DeckImplTest {

    @Test
    @DisplayName("Draw a card from an empty deck -> throws EmptyDeckException")
    fun drawACardFromEmptyDeckThrowsException() {
        val deck = DeckImpl()
        assertThat(deck.size()).isEqualTo(0)

        val failure = assertThat { deck.draw() }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Trying to draw a card from an empty deck")
    }
    @Test
    @DisplayName("Draw two cards from a one card deck -> throws EmptyDeckException")
    fun drawTwoCardsFromOneCardDeckThrowsException() {
        val deck = DeckImpl(mutableListOf(aCard()))
        assertThat(deck.size()).isEqualTo(1)

        val failure = assertThat { deck.draw(2) }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Trying to draw a card from an empty deck")
    }

    @Test
    @DisplayName("A three cards deck -> draw those cards")
    fun addThreeCardAndDrawThoseCards() {
        val cardA = cardOf(rankOf('A'), aSeed())
        val cardB = cardOf(rankOf('B'), aSeed())
        val cardC = cardOf(rankOf('C'), aSeed())
        val deck = DeckImpl(mutableListOf(cardA,cardB,cardC))
        assertThat(deck.size()).isEqualTo(3)
        assertThat(deckContent(deck)).containsOnly(cardA,cardC,cardB)
        assertThat(deck.size()).isEqualTo(0)
    }

}
