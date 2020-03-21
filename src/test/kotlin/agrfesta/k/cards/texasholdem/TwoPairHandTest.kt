package agrfesta.k.cards.texasholdem

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand
import agrfesta.k.cards.texasholdem.rules.hands.TwoPairHand
import agrfesta.kcards.playingcards.suits.FrenchRank.*
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("TWO PAIR tests")
class TwoPairHandTest {

    @TestFactory
    @DisplayName("comparisons")
    fun comparisons() = listOf(
            HECompareAssertionData(
                    TwoPairHand(THREE,TWO,   JACK),
                    TwoPairHand(QUEEN,TWO,   JACK),
                    -1),
            HECompareAssertionData(
                    TwoPairHand(JACK,TEN,   ACE),
                    TwoPairHand(QUEEN,TWO,   THREE),
                    -1),
            HECompareAssertionData(
                    TwoPairHand(NINE,SEVEN,   TWO),
                    TwoPairHand(NINE,FOUR,   JACK),
                    1),
            HECompareAssertionData(
                    TwoPairHand(FIVE,TWO,   KING),
                    TwoPairHand(FIVE,TWO,   KING),
                    0),
            HECompareAssertionData(
                    TwoPairHand(NINE,SEVEN,   KING),
                    TwoPairHand(NINE,SEVEN,   JACK),
                    1)
    ).map { createDynamicTest(it) }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandImplementationRaiseAnException() {
        val hand = TwoPairHand(NINE,SEVEN,   KING)
        val he: CardsEvaluation = object : CardsEvaluation {
            override fun compareTo(other: CardsEvaluation): Int = 0
            override fun getHandValue(): THPokerHand = THPokerHand.TWO_PAIR
        }

        val failure = assertThat {
            hand.compareTo(he)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Comparable only to an instance of TwoPairHand")
    }

    @Test
    @DisplayName("Pairs rank are equal -> raises an Exception")
    fun pairsRankAreEqualRaisesException() {
        val failure = assertThat {
            TwoPairHand(NINE,NINE,   KING)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Pairs rank can't be equal: NINE")
    }
    @Test
    @DisplayName("Major pair rank is equal to kicker rank -> raises an Exception")
    fun majorPairRankIsEqualToKickerRankRaisesAnException() {
        val failure = assertThat {
            TwoPairHand(NINE,SIX,   NINE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Pairs rank can't be equal to kicker rank: NINE")
    }
    @Test
    @DisplayName("Minor pair rank is equal to kicker rank -> raises an Exception")
    fun minorPairRankIsEqualToKickerRankRaisesAnException() {
        val failure = assertThat {
            TwoPairHand(NINE,SIX,   SIX)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Pairs rank can't be equal to kicker rank: SIX")
    }
    @Test
    @DisplayName("Minor pair rank is greater than major pair rank -> raises an Exception")
    fun minorPairRankIsGreaterThanMajorRankRaisesAnException() {
        val failure = assertThat {
            TwoPairHand(NINE,ACE,   SIX)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Minor pair rank can't be greater than major pair: NINE,ACE")
    }
}