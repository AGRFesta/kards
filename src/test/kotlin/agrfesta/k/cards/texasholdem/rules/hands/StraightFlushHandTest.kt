package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.createDynamicTest
import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.kcards.playingcards.suits.*
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
            HECompareAssertionData(
                    StraightFlushHand(FIVE, FrenchSeed.HEARTS),
                    StraightFlushHand(QUEEN, FrenchSeed.HEARTS),
                    -1),
            HECompareAssertionData(
                    StraightFlushHand(ACE, FrenchSeed.DIAMONDS),
                    StraightFlushHand(NINE, FrenchSeed.HEARTS),
                    1),
            HECompareAssertionData(
                    StraightFlushHand(SEVEN, FrenchSeed.DIAMONDS),
                    StraightFlushHand(SEVEN, FrenchSeed.HEARTS),
                    0)
    ).map { createDynamicTest(it) }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandEvaluationImplementationRaiseAnException() {
        val sfh = StraightFlushHand(SEVEN, FrenchSeed.HEARTS)
        val ce: CardsEvaluation = object : CardsEvaluation {
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
    @DisplayName("Straight top is FOUR -> raises an Exception")
    fun straightTopIsFourRaisesAnException() {
        val failure = assertThat {
            StraightFlushHand(FOUR, FrenchSeed.HEARTS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Straight top is FIVE, straightTop: FOUR")
    }
    @Test
    @DisplayName("Straight top is THREE -> raises an Exception")
    fun straightTopIsThreeRaisesAnException() {
        val failure = assertThat {
            StraightFlushHand(THREE, FrenchSeed.HEARTS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Straight top is FIVE, straightTop: THREE")
    }
    @Test
    @DisplayName("Straight top is TWO -> raises an Exception")
    fun straightTopIsTwoRaisesAnException() {
        val failure = assertThat {
            StraightFlushHand(TWO, FrenchSeed.HEARTS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Straight top is FIVE, straightTop: TWO")
    }
}