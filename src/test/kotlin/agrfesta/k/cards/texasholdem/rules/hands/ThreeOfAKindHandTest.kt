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

@DisplayName("THREE OF A KIND tests")
class ThreeOfAKindHandTest {

    @TestFactory
    @DisplayName("comparisons")
    fun comparisons() = listOf(
            HECompareAssertionData(
                    ThreeOfAKindHand(THREE, ACE, JACK),
                    ThreeOfAKindHand(QUEEN, KING, JACK),
                    -1),
            HECompareAssertionData(
                    ThreeOfAKindHand(NINE, ACE, TWO),
                    ThreeOfAKindHand(NINE, KING, JACK),
                    1),
            HECompareAssertionData(
                    ThreeOfAKindHand(FIVE, ACE, KING),
                    ThreeOfAKindHand(FIVE, ACE, KING),
                    0),
            HECompareAssertionData(
                    ThreeOfAKindHand(NINE, ACE, KING),
                    ThreeOfAKindHand(NINE, ACE, JACK),
                    1)
    ).map { createDynamicTest(it) }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandEvaluationImplementationRaiseAnException() {
        val hcet = ThreeOfAKindHand(ACE,   SEVEN, KING)
        val he: CardsEvaluation = object : CardsEvaluation {
            override fun compareTo(other: CardsEvaluation): Int = 0
            override fun getHandValue(): THPokerHand = THPokerHand.THREE_OF_A_KIND
        }

        val failure = assertThat {
            hcet.compareTo(he)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Comparable only to an instance of ThreeOfAKindHand")
    }

    @Test
    @DisplayName("First kicker is equals to tokRank -> raises an Exception")
    fun firstKickerIsEqualsToTokRankRaisesAnException() {
        val failure = assertThat {
            ThreeOfAKindHand(ACE,   ACE, KING)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Rank of kickers can't be equal to tokRank: ACE")
    }
    @Test
    @DisplayName("Second kicker is equals to tokRank -> raises an Exception")
    fun secondKickerIsEqualsToTokRankRaisesAnException() {
        val failure = assertThat {
            ThreeOfAKindHand(KING,   ACE, KING)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Rank of kickers can't be equal to tokRank: KING")
    }

    @Test
    @DisplayName("Create from not sorted kickers -> kickers are sorted")
    fun createFromUnorderedKickersResultInOrderedKickers() {
        val hand = ThreeOfAKindHand(JACK,   TWO, ACE)
        assertThat(hand.kickers).containsExactly(ACE, TWO)
    }

    @Test
    @DisplayName("Kickers have the same Rank -> raises an Exception")
    fun kickersHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            ThreeOfAKindHand(JACK,   ACE, ACE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Kickers have same Rank: ACE,ACE")
    }

}