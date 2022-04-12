package org.agrfesta.k.kards.texasholdem.rules.hands

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.agrfesta.k.cards.playingcards.suits.ACE
import org.agrfesta.k.cards.playingcards.suits.EIGHT
import org.agrfesta.k.cards.playingcards.suits.FIVE
import org.agrfesta.k.cards.playingcards.suits.FOUR
import org.agrfesta.k.cards.playingcards.suits.FrenchSeed.CLUBS
import org.agrfesta.k.cards.playingcards.suits.FrenchSeed.DIAMONDS
import org.agrfesta.k.cards.playingcards.suits.FrenchSeed.HEARTS
import org.agrfesta.k.cards.playingcards.suits.FrenchSeed.SPADES
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

@DisplayName("FLUSH tests")
class FlushHandTest {

    @TestFactory
    @DisplayName("comparisons")
    fun comparisons() = listOf(
            willAssertThat(FlushHand(THREE, ACE, JACK, TEN, TWO, HEARTS))
                    .isGreaterThan(
                        FlushHand(
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
                        FlushHand(
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
                        FlushHand(
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
                        FlushHand(
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
                        FlushHand(
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
                        FlushHand(
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
        val fh = FlushHand(NINE, ACE, KING, JACK, SEVEN, CLUBS)
        val he: CardsEvaluation = object : CardsEvaluation {
            override fun compareTo(other: CardsEvaluation): Int = 0
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
        val fh = FlushHand(THREE, JACK, TWO, KING, ACE, CLUBS)
        assertThat(fh.ranks).containsExactly(ACE,KING,JACK,THREE,TWO)
    }

    @Test
    @DisplayName("Two have the same Rank -> raises an Exception")
    fun twoHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushHand(JACK, ACE, KING, ACE, TWO, CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: J,A,K,A,2")
    }
    @Test
    @DisplayName("Three have the same Rank -> raises an Exception")
    fun threeHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushHand(JACK, ACE, ACE, ACE, TWO, CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: J,A,A,A,2")
    }
    @Test
    @DisplayName("Four have the same Rank -> raises an Exception")
    fun fourHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushHand(ACE, ACE, ACE, ACE, TWO, CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: A,A,A,A,2")
    }
    @Test
    @DisplayName("All have the same Rank -> raises an Exception")
    fun allHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushHand(ACE, ACE, ACE, ACE, ACE, CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: A,A,A,A,A")
    }
}
