package org.agrfesta.k.kards.texasholdem.rules.hands

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.agrfesta.k.cards.playingcards.suits.ACE
import org.agrfesta.k.cards.playingcards.suits.FIVE
import org.agrfesta.k.cards.playingcards.suits.JACK
import org.agrfesta.k.cards.playingcards.suits.KING
import org.agrfesta.k.cards.playingcards.suits.NINE
import org.agrfesta.k.cards.playingcards.suits.QUEEN
import org.agrfesta.k.cards.playingcards.suits.SEVEN
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

@DisplayName("THREE OF A KIND tests")
class ThreeOfAKindHandTest {

    @TestFactory
    @DisplayName("comparisons")
    fun comparisons() = listOf(
            willAssertThat(ThreeOfAKindHand(THREE,ACE,JACK)).isLessThan(ThreeOfAKindHand(QUEEN,KING,JACK)),
            willAssertThat(ThreeOfAKindHand(NINE,ACE,TWO)).isGreaterThan(ThreeOfAKindHand(NINE,KING,JACK)),
            willAssertThat(ThreeOfAKindHand(FIVE,ACE,KING)).isEqualTo(ThreeOfAKindHand(FIVE,KING,ACE)),
            willAssertThat(ThreeOfAKindHand(NINE,ACE,KING)).isGreaterThan(ThreeOfAKindHand(NINE,ACE,JACK))
    ).map { createDynamicTest(it) }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandEvaluationImplementationRaiseAnException() {
        val hcet = ThreeOfAKindHand(ACE,   SEVEN, KING)
        val he: CardsEvaluation = object :
            CardsEvaluation {
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
        failure.hasMessage("Rank of kickers can't be equal to tokRank: A")
    }
    @Test
    @DisplayName("Second kicker is equals to tokRank -> raises an Exception")
    fun secondKickerIsEqualsToTokRankRaisesAnException() {
        val failure = assertThat {
            ThreeOfAKindHand(KING,   ACE, KING)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Rank of kickers can't be equal to tokRank: K")
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
        failure.hasMessage("Kickers have same Rank: A,A")
    }

}
