package agrfesta.k.cards.texasholdem

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.hands.PairHand
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand
import agrfesta.kcards.playingcards.suits.FrenchRank.*
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("ONE PAIR tests")
class PairHandTest {

    @TestFactory
    @DisplayName("comparisons")
    fun sameEvaluationTypesCompareOnRankAndKickers() = listOf(
            HECompareAssertionData(
                    PairHand(THREE, ACE, JACK, TEN),
                    PairHand(QUEEN, KING, JACK, FIVE),
                    -1),
            HECompareAssertionData(
                    PairHand(NINE, ACE, TWO, THREE),
                    PairHand(NINE, KING, JACK, TEN),
                    1),
            HECompareAssertionData(
                    PairHand(FIVE, ACE, KING, JACK),
                    PairHand(FIVE, ACE, KING, JACK),
                    0),
            HECompareAssertionData(
                    PairHand(NINE, ACE, KING, JACK),
                    PairHand(NINE, ACE, JACK, TEN),
                    1),
            HECompareAssertionData(
                    PairHand(NINE, ACE, KING, JACK),
                    PairHand(NINE, ACE, KING, TEN),
                    1)
    )
        .map { data ->
            DynamicTest.dynamicTest(data.getTitle()) { HECompareAssertionData::assertComparison }
        }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandEvaluationImplementationRaiseAnException() {
        val hcet = PairHand(ACE, SEVEN, KING, JACK)
        val he: CardsEvaluation = object : CardsEvaluation {
            override fun compareTo(other: CardsEvaluation): Int = 0
            override fun getHandValue(): THPokerHand = THPokerHand.PAIR
        }

        val failure = assertThat {
            hcet.compareTo(he)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Comparable only to an instance of OnePairEvaluation")
    }

    @Test
    @DisplayName("First kicker is equals to tokRank -> raises an Exception")
    fun firstKickerIsEqualsToTokRankRaisesAnException() {
        val failure = assertThat {
            PairHand(ACE, ACE, KING, JACK)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Rank of kickers can't be equal to pairRank: ACE")
    }
    @Test
    @DisplayName("Second kicker is equals to tokRank -> raises an Exception")
    fun secondKickerIsEqualsToTokRankRaisesAnException() {
        val failure = assertThat {
            PairHand(KING, ACE, KING, JACK)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Rank of kickers can't be equal to pairRank: KING")
    }
    @Test
    @DisplayName("Third kicker is equals to tokRank -> raises an Exception")
    fun thirdKickerIsEqualsToTokRankRaisesAnException() {
        val failure = assertThat {
            PairHand(JACK, ACE, KING, JACK)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Rank of kickers can't be equal to pairRank: JACK")
    }

    @Test
    @DisplayName("Create from not sorted kickers -> kickers are sorted")
    fun createFromUnorderedKickersResultInOrderedKickers() {
        val eval = PairHand(JACK, TWO, KING, ACE)
        assertThat(eval.kickers).containsExactly(ACE,KING,TWO)
    }

    @Test
    @DisplayName("Two kickers have the same Rank -> raises an Exception")
    fun twoKickersHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            PairHand(JACK, ACE, KING, ACE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple kickers with same Rank: ACE,KING,ACE")
    }
    @Test
    @DisplayName("All kickers have the same Rank -> raises an Exception")
    fun allKickersHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            PairHand(JACK, ACE, ACE, ACE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple kickers with same Rank: ACE,ACE,ACE")
    }

}