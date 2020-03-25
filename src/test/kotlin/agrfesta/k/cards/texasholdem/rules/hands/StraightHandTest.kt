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

@DisplayName("STRAIGHT tests")
class StraightHandTest {

    @TestFactory
    @DisplayName("comparisons")
    fun comparisons() = listOf(
            HECompareAssertionData(
                    StraightHand(FIVE),
                    StraightHand(QUEEN),
                    -1),
            HECompareAssertionData(
                    StraightHand(ACE),
                    StraightHand(NINE),
                    1),
            HECompareAssertionData(
                    StraightHand(SEVEN),
                    StraightHand(SEVEN),
                    0)
    ).map { createDynamicTest(it) }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandEvaluationImplementationRaiseAnException() {
        val sh = StraightHand(ACE)
        val ce: CardsEvaluation = object : CardsEvaluation {
            override fun compareTo(other: CardsEvaluation): Int = 0
            override fun getHandValue(): THPokerHand = THPokerHand.STRAIGHT
        }

        val failure = assertThat {
            sh.compareTo(ce)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Comparable only to an instance of StraightHand")
    }

    @Test
    @DisplayName("Straight top is FOUR -> raises an Exception")
    fun straightTopIsFourRaisesAnException() {
        val failure = assertThat {
            StraightHand(FOUR)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Straight top is FIVE, straightTop: FOUR")
    }
    @Test
    @DisplayName("Straight top is THREE -> raises an Exception")
    fun straightTopIsThreeRaisesAnException() {
        val failure = assertThat {
            StraightHand(THREE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Straight top is FIVE, straightTop: THREE")
    }
    @Test
    @DisplayName("Straight top is TWO -> raises an Exception")
    fun straightTopIsTwoRaisesAnException() {
        val failure = assertThat {
            StraightHand(TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Straight top is FIVE, straightTop: TWO")
    }
}