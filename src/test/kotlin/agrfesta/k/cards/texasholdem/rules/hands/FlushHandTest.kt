package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.createDynamicTest
import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.kcards.playingcards.suits.*
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
            HECompareAssertionData(
                    FlushHand(THREE, ACE, JACK, TEN, TWO, FrenchSeed.HEARTS),
                    FlushHand(QUEEN, KING, JACK, FIVE, TEN, FrenchSeed.HEARTS),
                    1),
            HECompareAssertionData(
                    FlushHand(NINE, ACE, TWO, THREE, FOUR, FrenchSeed.HEARTS),
                    FlushHand(ACE, KING, JACK, TEN, FOUR, FrenchSeed.SPADES),
                    -1),
            HECompareAssertionData(
                    FlushHand(FIVE, ACE, KING, NINE, SEVEN, FrenchSeed.HEARTS),
                    FlushHand(FIVE, ACE, KING, JACK, SIX, FrenchSeed.HEARTS),
                    -1),
            HECompareAssertionData(
                    FlushHand(NINE, ACE, KING, JACK, TEN, FrenchSeed.DIAMONDS),
                    FlushHand(NINE, ACE, JACK, KING, FIVE, FrenchSeed.HEARTS),
                    1),
            HECompareAssertionData(
                    FlushHand(NINE, ACE, KING, JACK, EIGHT, FrenchSeed.HEARTS),
                    FlushHand(NINE, ACE, KING, JACK, SEVEN, FrenchSeed.HEARTS),
                    1),
            HECompareAssertionData(
                    FlushHand(NINE, ACE, KING, JACK, SEVEN, FrenchSeed.CLUBS),
                    FlushHand(NINE, ACE, KING, JACK, SEVEN, FrenchSeed.HEARTS),
                    0)
    ).map { createDynamicTest(it) }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandEvaluationImplementationRaiseAnException() {
        val fh = FlushHand(NINE, ACE, KING, JACK, SEVEN, FrenchSeed.CLUBS)
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
        val fh = FlushHand(THREE, JACK, TWO, KING, ACE, FrenchSeed.CLUBS)
        assertThat(fh.ranks).containsExactly(ACE,KING,JACK,THREE,TWO)
    }

    @Test
    @DisplayName("Two have the same Rank -> raises an Exception")
    fun twoHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushHand(JACK, ACE, KING, ACE, TWO, FrenchSeed.CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: JACK,ACE,KING,ACE,TWO")
    }
    @Test
    @DisplayName("Three have the same Rank -> raises an Exception")
    fun threeHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushHand(JACK, ACE, ACE, ACE, TWO, FrenchSeed.CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: JACK,ACE,ACE,ACE,TWO")
    }
    @Test
    @DisplayName("Four have the same Rank -> raises an Exception")
    fun fourHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushHand(ACE, ACE, ACE, ACE, TWO, FrenchSeed.CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: ACE,ACE,ACE,ACE,TWO")
    }
    @Test
    @DisplayName("All have the same Rank -> raises an Exception")
    fun allHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushHand(ACE, ACE, ACE, ACE, ACE, FrenchSeed.CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: ACE,ACE,ACE,ACE,ACE")
    }
}