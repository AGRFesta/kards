package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.suits.ACE
import agrfesta.k.cards.playingcards.suits.FIVE
import agrfesta.k.cards.playingcards.suits.FOUR
import agrfesta.k.cards.playingcards.suits.JACK
import agrfesta.k.cards.playingcards.suits.KING
import agrfesta.k.cards.playingcards.suits.NINE
import agrfesta.k.cards.playingcards.suits.QUEEN
import agrfesta.k.cards.playingcards.suits.SEVEN
import agrfesta.k.cards.playingcards.suits.SIX
import agrfesta.k.cards.playingcards.suits.TEN
import agrfesta.k.cards.playingcards.suits.THREE
import agrfesta.k.cards.playingcards.suits.TWO
import agrfesta.k.cards.texasholdem.createDynamicTest
import agrfesta.k.cards.texasholdem.isEqualTo
import agrfesta.k.cards.texasholdem.isGreaterThan
import agrfesta.k.cards.texasholdem.isLessThan
import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.willAssertThat
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
            willAssertThat(TwoPairHand(THREE,TWO,JACK)).isLessThan(TwoPairHand(QUEEN,TWO,JACK)),
            willAssertThat(TwoPairHand(JACK,TEN,ACE)).isLessThan(TwoPairHand(QUEEN,TWO,THREE)),
            willAssertThat(TwoPairHand(NINE,SEVEN,TWO)).isGreaterThan(TwoPairHand(NINE,FOUR,JACK)),
            willAssertThat(TwoPairHand(FIVE,TWO,KING)).isEqualTo(TwoPairHand(FIVE,TWO,KING)),
            willAssertThat(TwoPairHand(NINE,SEVEN,KING)).isGreaterThan(TwoPairHand(NINE,SEVEN,JACK))
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
