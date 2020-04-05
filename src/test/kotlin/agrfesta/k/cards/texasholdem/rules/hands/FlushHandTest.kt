package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.*
import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.kcards.playingcards.suits.FrenchSeed.*
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
            willAssertThat(FlushHand(THREE,ACE,JACK,TEN,TWO, HEARTS))
                    .isGreaterThan(FlushHand(QUEEN,KING,JACK,FIVE,TEN, HEARTS)),
            willAssertThat(FlushHand(NINE,ACE,TWO,THREE,FOUR, HEARTS))
                    .isLessThan(FlushHand(ACE,KING,JACK,TEN,FOUR, SPADES)),
            willAssertThat(FlushHand(FIVE,ACE,KING,NINE,SEVEN, HEARTS))
                    .isLessThan(FlushHand(FIVE,ACE,KING,JACK,SIX, HEARTS)),
            willAssertThat(FlushHand(NINE,ACE,KING,JACK,TEN, DIAMONDS))
                    .isGreaterThan(FlushHand(NINE,ACE,JACK,KING,FIVE, HEARTS)),
            willAssertThat(FlushHand(NINE,ACE,KING,JACK,EIGHT, HEARTS))
                    .isGreaterThan(FlushHand(NINE,ACE,KING,JACK,SEVEN, HEARTS)),
            willAssertThat(FlushHand(NINE,ACE,KING,JACK,SEVEN, CLUBS))
                    .isEqualTo(FlushHand(NINE,ACE,KING,JACK,SEVEN, HEARTS))
    ).map { createDynamicTest(it) }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandEvaluationImplementationRaiseAnException() {
        val fh = FlushHand(NINE,ACE,KING,JACK,SEVEN, CLUBS)
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
        val fh = FlushHand(THREE,JACK,TWO,KING,ACE, CLUBS)
        assertThat(fh.ranks).containsExactly(ACE,KING,JACK,THREE,TWO)
    }

    @Test
    @DisplayName("Two have the same Rank -> raises an Exception")
    fun twoHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushHand(JACK,ACE,KING,ACE,TWO, CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: JACK,ACE,KING,ACE,TWO")
    }
    @Test
    @DisplayName("Three have the same Rank -> raises an Exception")
    fun threeHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushHand(JACK,ACE,ACE,ACE,TWO, FrenchSeed.CLUBS)
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