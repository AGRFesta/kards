package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.suits.ACE
import agrfesta.k.cards.playingcards.suits.FIVE
import agrfesta.k.cards.playingcards.suits.JACK
import agrfesta.k.cards.playingcards.suits.KING
import agrfesta.k.cards.playingcards.suits.NINE
import agrfesta.k.cards.playingcards.suits.QUEEN
import agrfesta.k.cards.playingcards.suits.SEVEN
import agrfesta.k.cards.playingcards.suits.TEN
import agrfesta.k.cards.playingcards.suits.THREE
import agrfesta.k.cards.playingcards.suits.TWO
import agrfesta.k.cards.texasholdem.createDynamicTest
import agrfesta.k.cards.texasholdem.isEqualTo
import agrfesta.k.cards.texasholdem.isGreaterThan
import agrfesta.k.cards.texasholdem.isLessThan
import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.willAssertThat
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("PAIR tests")
class PairHandTest {

    @TestFactory
    @DisplayName("comparisons")
    fun comparisons() = listOf(
            willAssertThat(PairHand(THREE,ACE,JACK,TEN)).isLessThan(PairHand(QUEEN,KING,JACK,FIVE)),
            willAssertThat(PairHand(NINE,ACE,TWO,THREE)).isGreaterThan(PairHand(NINE,KING,JACK,TEN)),
            willAssertThat(PairHand(FIVE,ACE,KING,JACK)).isEqualTo(PairHand(FIVE,ACE,KING,JACK)),
            willAssertThat(PairHand(NINE,ACE,KING,JACK)).isGreaterThan(PairHand(NINE,ACE,JACK,TEN)),
            willAssertThat(PairHand(NINE,ACE,KING,JACK)).isGreaterThan(PairHand(NINE,ACE,KING,TEN))
    ).map { createDynamicTest(it) }

    @Test
    @DisplayName("comparing to a different evaluation -> raises an Exception")
    fun compareToADifferentHandEvaluationImplementationRaiseAnException() {
        val ph = PairHand(ACE,   SEVEN, KING, JACK)
        val he: CardsEvaluation = object : CardsEvaluation {
            override fun compareTo(other: CardsEvaluation): Int = 0
            override fun getHandValue(): THPokerHand = THPokerHand.PAIR
        }

        val failure = assertThat {
            ph.compareTo(he)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Comparable only to an instance of PairHand")
    }

    @Test
    @DisplayName("First kicker is equals to pairRank -> raises an Exception")
    fun firstKickerIsEqualsToPairRankRaisesAnException() {
        val failure = assertThat {
            PairHand(ACE,   ACE, KING, JACK)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Rank of kickers can't be equal to pairRank: ACE")
    }
    @Test
    @DisplayName("Second kicker is equals to pairRank -> raises an Exception")
    fun secondKickerIsEqualsToPairRankRaisesAnException() {
        val failure = assertThat {
            PairHand(KING,   ACE, KING, JACK)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Rank of kickers can't be equal to pairRank: KING")
    }
    @Test
    @DisplayName("Third kicker is equals to pairRank -> raises an Exception")
    fun thirdKickerIsEqualsToPairRankRaisesAnException() {
        val failure = assertThat {
            PairHand(JACK,   ACE, KING, JACK)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Rank of kickers can't be equal to pairRank: JACK")
    }

    @Test
    @DisplayName("Create from not sorted kickers -> kickers are sorted")
    fun createFromUnorderedKickersResultInOrderedKickers() {
        val ph = PairHand(JACK,   KING, TWO, ACE)
        assertThat(ph.kickers).containsExactly(ACE,KING,TWO)
    }

    @Test
    @DisplayName("Two kickers have the same Rank -> raises an Exception")
    fun twoKickersHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            PairHand(JACK,   ACE, KING, ACE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple kickers with same Rank: ACE,KING,ACE")
    }
    @Test
    @DisplayName("All kickers have the same Rank -> raises an Exception")
    fun allKickersHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            PairHand(JACK,   ACE, ACE, ACE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple kickers with same Rank: ACE,ACE,ACE")
    }

}
