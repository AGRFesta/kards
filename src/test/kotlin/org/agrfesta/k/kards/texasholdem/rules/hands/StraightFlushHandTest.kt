package org.agrfesta.k.kards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.suits.ACE
import agrfesta.k.cards.playingcards.suits.FIVE
import agrfesta.k.cards.playingcards.suits.FOUR
import agrfesta.k.cards.playingcards.suits.FrenchSeed.DIAMONDS
import agrfesta.k.cards.playingcards.suits.FrenchSeed.HEARTS
import agrfesta.k.cards.playingcards.suits.NINE
import agrfesta.k.cards.playingcards.suits.QUEEN
import agrfesta.k.cards.playingcards.suits.SEVEN
import agrfesta.k.cards.playingcards.suits.THREE
import agrfesta.k.cards.playingcards.suits.TWO
import org.agrfesta.k.kards.texasholdem.createDynamicTest
import org.agrfesta.k.kards.texasholdem.isEqualTo
import org.agrfesta.k.kards.texasholdem.isGreaterThan
import org.agrfesta.k.kards.texasholdem.isLessThan
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation
import org.agrfesta.k.kards.texasholdem.willAssertThat
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("STRAIGHT FLUSH tests")
class StraightFlushHandTest {

    @TestFactory
    @DisplayName("comparisons")
    fun comparisons() = listOf(
            willAssertThat(StraightFlushHand(FIVE, HEARTS)).isLessThan(StraightFlushHand(QUEEN, HEARTS)),
            willAssertThat(StraightFlushHand(ACE, DIAMONDS)).isGreaterThan(StraightFlushHand(NINE, HEARTS)),
            willAssertThat(StraightFlushHand(SEVEN, DIAMONDS)).isEqualTo(StraightFlushHand(SEVEN, HEARTS))
    ).map { createDynamicTest(it) }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandEvaluationImplementationRaiseAnException() {
        val sfh = StraightFlushHand(SEVEN, HEARTS)
        val ce: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation = object :
            org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation {
            override fun compareTo(other: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation): Int = 0
            override fun getHandValue(): THPokerHand = THPokerHand.STRAIGHT_FLUSH
        }

        val failure = assertThat {
            sfh.compareTo(ce)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Comparable only to an instance of StraightFlushHand")
    }

    @Test
    @DisplayName("Straight top is FOUR -> raises an Exception")
    fun straightTopIsFourRaisesAnException() {
        val failure = assertThat {
            StraightFlushHand(FOUR, HEARTS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Straight top is FIVE, straightTop: FOUR")
    }
    @Test
    @DisplayName("Straight top is THREE -> raises an Exception")
    fun straightTopIsThreeRaisesAnException() {
        val failure = assertThat {
            StraightFlushHand(THREE, HEARTS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Straight top is FIVE, straightTop: THREE")
    }
    @Test
    @DisplayName("Straight top is TWO -> raises an Exception")
    fun straightTopIsTwoRaisesAnException() {
        val failure = assertThat {
            StraightFlushHand(TWO, HEARTS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Straight top is FIVE, straightTop: TWO")
    }
}
