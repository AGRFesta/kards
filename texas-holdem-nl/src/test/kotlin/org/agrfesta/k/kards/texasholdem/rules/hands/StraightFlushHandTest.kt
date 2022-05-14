package org.agrfesta.k.kards.texasholdem.rules.hands

import org.agrfesta.k.cards.playingcards.suits.ACE
import org.agrfesta.k.cards.playingcards.suits.FIVE
import org.agrfesta.k.cards.playingcards.suits.FOUR
import org.agrfesta.k.cards.playingcards.suits.FrenchSeed.DIAMONDS
import org.agrfesta.k.cards.playingcards.suits.FrenchSeed.HEARTS
import org.agrfesta.k.cards.playingcards.suits.NINE
import org.agrfesta.k.cards.playingcards.suits.QUEEN
import org.agrfesta.k.cards.playingcards.suits.SEVEN
import org.agrfesta.k.cards.playingcards.suits.THREE
import org.agrfesta.k.cards.playingcards.suits.TWO
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.agrfesta.k.kards.texasholdem.createDynamicTest
import org.agrfesta.k.kards.texasholdem.isEqualTo
import org.agrfesta.k.kards.texasholdem.isGreaterThan
import org.agrfesta.k.kards.texasholdem.isLessThan
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation
import org.agrfesta.k.kards.texasholdem.willAssertThat
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
        val ce: CardsEvaluation = object :
            CardsEvaluation {
            override fun compareTo(other: CardsEvaluation): Int = 0
            override fun getHandValue(): THPokerHand = THPokerHand.STRAIGHT_FLUSH
        }

        val failure = assertThat {
            sfh.compareTo(ce)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Comparable only to an instance of StraightFlushHand")
    }

    @Test
    @DisplayName("Straight top is 4 -> raises an Exception")
    fun straightTopIsFourRaisesAnException() {
        val failure = assertThat {
            StraightFlushHand(FOUR, HEARTS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Straight top is 5, straightTop: 4")
    }
    @Test
    @DisplayName("Straight top is 3-> raises an Exception")
    fun straightTopIsThreeRaisesAnException() {
        val failure = assertThat {
            StraightFlushHand(THREE, HEARTS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Straight top is 5, straightTop: 3")
    }
    @Test
    @DisplayName("Straight top is 2-> raises an Exception")
    fun straightTopIsTwoRaisesAnException() {
        val failure = assertThat {
            StraightFlushHand(TWO, HEARTS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Straight top is 5, straightTop: 2")
    }
}
