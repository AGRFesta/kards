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

@DisplayName("FOUR OF A KIND tests")
class FourOfAKindHandTest {

    @TestFactory
    @DisplayName("comparisons")
    fun comparisons() = listOf(
            HECompareAssertionData(
                    FourOfAKindHand(THREE, TWO),
                    FourOfAKindHand(QUEEN, TWO),
                    -1),
            HECompareAssertionData(
                    FourOfAKindHand(JACK, TEN),
                    FourOfAKindHand(QUEEN, TWO),
                    -1),
            HECompareAssertionData(
                    FourOfAKindHand(NINE, SEVEN),
                    FourOfAKindHand(NINE, FOUR),
                    1),
            HECompareAssertionData(
                    FourOfAKindHand(FIVE, TWO),
                    FourOfAKindHand(FIVE, TWO),
                    0)
    ).map { createDynamicTest(it) }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandImplementationRaiseAnException() {
        val fokh = FourOfAKindHand(NINE, SEVEN)
        val he: CardsEvaluation = object : CardsEvaluation {
            override fun compareTo(other: CardsEvaluation): Int = 0
            override fun getHandValue(): THPokerHand = THPokerHand.FOUR_OF_A_KIND
        }

        val failure = assertThat {
            fokh.compareTo(he)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Comparable only to an instance of FourOfAKindHand")
    }

    @Test
    @DisplayName("Kickers rank is equal to fokRank -> raises an Exception")
    fun majorPairRankIsEqualToKickerRankRaisesAnException() {
        val failure = assertThat {
            FourOfAKindHand(NINE, NINE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Kicker's rank can't be equal to fokRank: NINE")
    }
}