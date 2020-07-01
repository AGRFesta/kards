package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.*
import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.playingcards.suits.*
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("FULL HOUSE tests")
class FullHouseHandTest {

    @TestFactory
    @DisplayName("comparisons")
    fun comparisons() = listOf(
            willAssertThat(FullHouseHand(THREE, TWO)).isLessThan(FullHouseHand(QUEEN, TWO)),
            willAssertThat(FullHouseHand(JACK, TEN)).isLessThan(FullHouseHand(QUEEN, TWO)),
            willAssertThat(FullHouseHand(NINE, SEVEN)).isGreaterThan(FullHouseHand(NINE, FOUR)),
            willAssertThat(FullHouseHand(FIVE, TWO)).isEqualTo(FullHouseHand(FIVE, TWO))
    ).map { createDynamicTest(it) }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandImplementationRaiseAnException() {
        val fhh = FullHouseHand(NINE, SEVEN)
        val he: CardsEvaluation = object : CardsEvaluation {
            override fun compareTo(other: CardsEvaluation): Int = 0
            override fun getHandValue(): THPokerHand = THPokerHand.FULL_HOUSE
        }

        val failure = assertThat {
            fhh.compareTo(he)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Comparable only to an instance of FullHouseHand")
    }

    @Test
    @DisplayName("Ranks are equal -> raises an Exception")
    fun pairsRankAreEqualRaisesException() {
        val failure = assertThat {
            FullHouseHand(NINE, NINE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Ranks can't be equal: NINE")
    }

}