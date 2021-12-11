package org.agrfesta.k.kards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.suits.FIVE
import agrfesta.k.cards.playingcards.suits.FOUR
import agrfesta.k.cards.playingcards.suits.JACK
import agrfesta.k.cards.playingcards.suits.NINE
import agrfesta.k.cards.playingcards.suits.QUEEN
import agrfesta.k.cards.playingcards.suits.SEVEN
import agrfesta.k.cards.playingcards.suits.TEN
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

@DisplayName("FOUR OF A KIND tests")
class FourOfAKindHandTest {

    @TestFactory
    @DisplayName("comparisons")
    fun comparisons() = listOf(
            willAssertThat(FourOfAKindHand(THREE, TWO)).isLessThan(FourOfAKindHand(QUEEN, TWO)),
            willAssertThat(FourOfAKindHand(JACK, TEN)).isLessThan(FourOfAKindHand(QUEEN, TWO)),
            willAssertThat(FourOfAKindHand(NINE, SEVEN)).isGreaterThan(FourOfAKindHand(NINE, FOUR)),
            willAssertThat(FourOfAKindHand(FIVE, TWO)).isEqualTo(FourOfAKindHand(FIVE, TWO))
    ).map { createDynamicTest(it) }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandImplementationRaiseAnException() {
        val fokh = FourOfAKindHand(NINE, SEVEN)
        val he: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation = object :
            org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation {
            override fun compareTo(other: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation): Int = 0
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
