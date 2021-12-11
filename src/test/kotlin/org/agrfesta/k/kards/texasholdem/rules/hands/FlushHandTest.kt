package org.agrfesta.k.kards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.suits.ACE
import agrfesta.k.cards.playingcards.suits.EIGHT
import agrfesta.k.cards.playingcards.suits.FIVE
import agrfesta.k.cards.playingcards.suits.FOUR
import agrfesta.k.cards.playingcards.suits.FrenchSeed.CLUBS
import agrfesta.k.cards.playingcards.suits.FrenchSeed.DIAMONDS
import agrfesta.k.cards.playingcards.suits.FrenchSeed.HEARTS
import agrfesta.k.cards.playingcards.suits.FrenchSeed.SPADES
import agrfesta.k.cards.playingcards.suits.JACK
import agrfesta.k.cards.playingcards.suits.KING
import agrfesta.k.cards.playingcards.suits.NINE
import agrfesta.k.cards.playingcards.suits.QUEEN
import agrfesta.k.cards.playingcards.suits.SEVEN
import agrfesta.k.cards.playingcards.suits.SIX
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
import assertk.assertions.containsExactly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("FLUSH tests")
class FlushHandTest {

    @TestFactory
    @DisplayName("comparisons")
    fun comparisons() = listOf(
            willAssertThat(FlushHand(THREE, ACE, JACK, TEN, TWO, HEARTS))
                    .isGreaterThan(
                        org.agrfesta.k.kards.texasholdem.rules.hands.FlushHand(
                            QUEEN,
                            KING,
                            JACK,
                            FIVE,
                            TEN,
                            HEARTS
                        )
                    ),
            willAssertThat(FlushHand(NINE, ACE, TWO, THREE, FOUR, HEARTS))
                    .isLessThan(
                        org.agrfesta.k.kards.texasholdem.rules.hands.FlushHand(
                            ACE,
                            KING,
                            JACK,
                            TEN,
                            FOUR,
                            SPADES
                        )
                    ),
            willAssertThat(FlushHand(FIVE, ACE, KING, NINE, SEVEN, HEARTS))
                    .isLessThan(
                        org.agrfesta.k.kards.texasholdem.rules.hands.FlushHand(
                            FIVE,
                            ACE,
                            KING,
                            JACK,
                            SIX,
                            HEARTS
                        )
                    ),
            willAssertThat(FlushHand(NINE, ACE, KING, JACK, TEN, DIAMONDS))
                    .isGreaterThan(
                        org.agrfesta.k.kards.texasholdem.rules.hands.FlushHand(
                            NINE,
                            ACE,
                            JACK,
                            KING,
                            FIVE,
                            HEARTS
                        )
                    ),
            willAssertThat(FlushHand(NINE, ACE, KING, JACK, EIGHT, HEARTS))
                    .isGreaterThan(
                        org.agrfesta.k.kards.texasholdem.rules.hands.FlushHand(
                            NINE,
                            ACE,
                            KING,
                            JACK,
                            SEVEN,
                            HEARTS
                        )
                    ),
            willAssertThat(FlushHand(NINE, ACE, KING, JACK, SEVEN, CLUBS))
                    .isEqualTo(
                        org.agrfesta.k.kards.texasholdem.rules.hands.FlushHand(
                            NINE,
                            ACE,
                            KING,
                            JACK,
                            SEVEN,
                            HEARTS
                        )
                    )
    ).map { createDynamicTest(it) }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandEvaluationImplementationRaiseAnException() {
        val fh = org.agrfesta.k.kards.texasholdem.rules.hands.FlushHand(NINE, ACE, KING, JACK, SEVEN, CLUBS)
        val he: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation = object :
            org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation {
            override fun compareTo(other: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation): Int = 0
            override fun getHandValue(): THPokerHand = THPokerHand.FLUSH
        }

        val failure = assertThat {
            fh.compareTo(he)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Comparable only to an instance of FlushHand")
    }

    @Test
    @DisplayName("Create from not sorted ranks -> ranks are sorted")
    fun createFromUnorderedRanksResultInOrderedRanks() {
        val fh = org.agrfesta.k.kards.texasholdem.rules.hands.FlushHand(THREE, JACK, TWO, KING, ACE, CLUBS)
        assertThat(fh.ranks).containsExactly(ACE,KING,JACK,THREE,TWO)
    }

    @Test
    @DisplayName("Two have the same Rank -> raises an Exception")
    fun twoHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            org.agrfesta.k.kards.texasholdem.rules.hands.FlushHand(JACK, ACE, KING, ACE, TWO, CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: JACK,ACE,KING,ACE,TWO")
    }
    @Test
    @DisplayName("Three have the same Rank -> raises an Exception")
    fun threeHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            org.agrfesta.k.kards.texasholdem.rules.hands.FlushHand(JACK, ACE, ACE, ACE, TWO, CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: JACK,ACE,ACE,ACE,TWO")
    }
    @Test
    @DisplayName("Four have the same Rank -> raises an Exception")
    fun fourHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            org.agrfesta.k.kards.texasholdem.rules.hands.FlushHand(ACE, ACE, ACE, ACE, TWO, CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: ACE,ACE,ACE,ACE,TWO")
    }
    @Test
    @DisplayName("All have the same Rank -> raises an Exception")
    fun allHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            org.agrfesta.k.kards.texasholdem.rules.hands.FlushHand(ACE, ACE, ACE, ACE, ACE, CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: ACE,ACE,ACE,ACE,ACE")
    }
}
