package org.agrfesta.k.kards.texasholdem.rules.hands

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.agrfesta.k.cards.playingcards.suits.ACE
import org.agrfesta.k.cards.playingcards.suits.EIGHT
import org.agrfesta.k.cards.playingcards.suits.FIVE
import org.agrfesta.k.cards.playingcards.suits.FOUR
import org.agrfesta.k.cards.playingcards.suits.JACK
import org.agrfesta.k.cards.playingcards.suits.KING
import org.agrfesta.k.cards.playingcards.suits.NINE
import org.agrfesta.k.cards.playingcards.suits.QUEEN
import org.agrfesta.k.cards.playingcards.suits.SEVEN
import org.agrfesta.k.cards.playingcards.suits.SIX
import org.agrfesta.k.cards.playingcards.suits.TEN
import org.agrfesta.k.cards.playingcards.suits.THREE
import org.agrfesta.k.cards.playingcards.suits.TWO
import org.agrfesta.k.kards.texasholdem.createDynamicTest
import org.agrfesta.k.kards.texasholdem.isEqualTo
import org.agrfesta.k.kards.texasholdem.isGreaterThan
import org.agrfesta.k.kards.texasholdem.isLessThan
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation
import org.agrfesta.k.kards.texasholdem.willAssertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("HIGH CARD tests")
class HighCardHandTest {

    @TestFactory
    @DisplayName("comparisons")
    fun comparisons() = listOf(
            willAssertThat(HighCardHand(THREE,ACE,JACK,TEN,TWO)).isGreaterThan(HighCardHand(QUEEN,KING,JACK,FIVE,TEN)),
            willAssertThat(HighCardHand(NINE,ACE,TWO,THREE,FOUR)).isLessThan(HighCardHand(ACE,KING,JACK,TEN,FOUR)),
            willAssertThat(HighCardHand(FIVE,ACE,KING,NINE,SEVEN)).isLessThan(HighCardHand(FIVE,ACE,KING,JACK,SIX)),
            willAssertThat(HighCardHand(NINE,ACE,KING,JACK,TEN)).isGreaterThan(HighCardHand(NINE,ACE,JACK,KING,FIVE)),
            willAssertThat(HighCardHand(NINE,ACE,KING,JACK,EIGHT)).isGreaterThan(
                    HighCardHand(NINE,ACE,KING,JACK,SEVEN)),
            willAssertThat(HighCardHand(NINE,ACE,KING,JACK,SEVEN)).isEqualTo(HighCardHand(NINE,KING,JACK,ACE,SEVEN))
    ).map { createDynamicTest(it) }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandEvaluationImplementationRaiseAnException() {
        val hcet = HighCardHand(ACE, SEVEN, KING, JACK, FIVE)
        val he: CardsEvaluation = object :
            CardsEvaluation {
            override fun compareTo(other: CardsEvaluation): Int = 0
            override fun getHandValue(): THPokerHand = THPokerHand.HIGH_CARD
        }

        val failure = assertThat {
            hcet.compareTo(he)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Comparable only to an instance of HighCardHand")
    }

    @Test
    @DisplayName("Create from not sorted kickers -> kickers are sorted")
    fun createFromUnorderedKickersResultInOrderedKickers() {
        val hand = HighCardHand(THREE, JACK, TWO, KING, ACE)
        assertThat(hand.kickers).containsExactly(ACE,KING,JACK,THREE,TWO)
    }

    @Test
    @DisplayName("Two kickers have the same Rank -> raises an Exception")
    fun twoKickersHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            HighCardHand(JACK, ACE, KING, ACE, TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple kickers with same Rank: J,A,K,A,2")
    }
    @Test
    @DisplayName("Three kickers have the same Rank -> raises an Exception")
    fun threeKickersHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            HighCardHand(JACK, ACE, ACE, ACE, TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple kickers with same Rank: J,A,A,A,2")
    }
    @Test
    @DisplayName("Four kickers have the same Rank -> raises an Exception")
    fun fourKickersHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            HighCardHand(ACE, ACE, ACE, ACE, TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple kickers with same Rank: A,A,A,A,2")
    }
    @Test
    @DisplayName("All kickers have the same Rank -> raises an Exception")
    fun allKickersHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            HighCardHand(ACE, ACE, ACE, ACE, ACE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple kickers with same Rank: A,A,A,A,A")
    }
}
