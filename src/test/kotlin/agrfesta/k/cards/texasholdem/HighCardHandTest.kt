package agrfesta.k.cards.texasholdem

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.hands.HighCardHand
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand
import agrfesta.kcards.playingcards.suits.*
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("HIGH CARD tests")
class HighCardHandTest {

    @TestFactory
    @DisplayName("comparisons")
    fun comparisons() = listOf(
        HECompareAssertionData(
                HighCardHand(THREE, ACE, JACK, TEN, TWO),
                HighCardHand(QUEEN, KING, JACK, FIVE, TEN),
                1),
        HECompareAssertionData(
                HighCardHand(NINE, ACE, TWO, THREE, FOUR),
                HighCardHand(ACE, KING, JACK, TEN, FOUR),
                -1),
        HECompareAssertionData(
                HighCardHand(FIVE, ACE, KING, NINE, SEVEN),
                HighCardHand(FIVE, ACE, KING, JACK, SIX),
                -1),
        HECompareAssertionData(
                HighCardHand(NINE, ACE, KING, JACK, TEN),
                HighCardHand(NINE, ACE, JACK, KING, FIVE),
                1),
        HECompareAssertionData(
                HighCardHand(NINE, ACE, KING, JACK, EIGHT),
                HighCardHand(NINE, ACE, KING, JACK, SEVEN),
                1),
        HECompareAssertionData(
                HighCardHand(NINE, ACE, KING, JACK, SEVEN),
                HighCardHand(NINE, ACE, KING, JACK, SEVEN),
                0)
    ).map { createDynamicTest(it) }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandEvaluationImplementationRaiseAnException() {
        val hcet = HighCardHand(ACE, SEVEN, KING, JACK, FIVE)
        val he: CardsEvaluation = object : CardsEvaluation {
            override fun compareTo(other: CardsEvaluation): Int = 0
            override fun getHandValue(): THPokerHand = THPokerHand.HIGH_CARD
        }

        val failure = assertThat {
            hcet.compareTo(he)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Comparable only to an instance of HighCardHand")
    }

    @Test
    @DisplayName("Create from not sorted kickers -> kickers are sorted")
    fun createFromUnorderedKickersResultInOrderedKickers() {
        val hand = HighCardHand(THREE, JACK, TWO, KING, ACE)
        assertThat(hand.kickers).containsExactly(ACE,KING,JACK,THREE,TWO)
    }

    @Test
    @DisplayName("Two kickers have the same Rank -> raises an Exception")
    fun twoKickersHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            HighCardHand(JACK, ACE, KING, ACE, TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple kickers with same Rank: JACK,ACE,KING,ACE,TWO")
    }
    @Test
    @DisplayName("Three kickers have the same Rank -> raises an Exception")
    fun threeKickersHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            HighCardHand(JACK, ACE, ACE, ACE, TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple kickers with same Rank: JACK,ACE,ACE,ACE,TWO")
    }
    @Test
    @DisplayName("Four kickers have the same Rank -> raises an Exception")
    fun fourKickersHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            HighCardHand(ACE, ACE, ACE, ACE, TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple kickers with same Rank: ACE,ACE,ACE,ACE,TWO")
    }
    @Test
    @DisplayName("All kickers have the same Rank -> raises an Exception")
    fun allKickersHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            HighCardHand(ACE, ACE, ACE, ACE, ACE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple kickers with same Rank: ACE,ACE,ACE,ACE,ACE")
    }
}