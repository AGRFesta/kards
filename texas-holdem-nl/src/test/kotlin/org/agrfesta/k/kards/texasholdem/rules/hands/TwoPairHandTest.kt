package org.agrfesta.k.kards.texasholdem.rules.hands

import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.agrfesta.k.cards.playingcards.suits.ACE
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
        val he: CardsEvaluation = object :
            CardsEvaluation {
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
        failure.hasMessage("Pairs rank can't be equal: 9")
    }
    @Test
    @DisplayName("Major pair rank is equal to kicker rank -> raises an Exception")
    fun majorPairRankIsEqualToKickerRankRaisesAnException() {
        val failure = assertThat {
            TwoPairHand(NINE,SIX,   NINE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Pairs rank can't be equal to kicker rank: 9")
    }
    @Test
    @DisplayName("Minor pair rank is equal to kicker rank -> raises an Exception")
    fun minorPairRankIsEqualToKickerRankRaisesAnException() {
        val failure = assertThat {
            TwoPairHand(NINE,SIX,   SIX)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Pairs rank can't be equal to kicker rank: 6")
    }
    @Test
    @DisplayName("Minor pair rank is greater than major pair rank -> raises an Exception")
    fun minorPairRankIsGreaterThanMajorRankRaisesAnException() {
        val failure = assertThat {
            TwoPairHand(NINE,ACE,   SIX)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Minor pair rank can't be greater than major pair: 9,A")
    }
}
