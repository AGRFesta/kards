package agrfesta.k.cards.texasholdem.draws

import agrfesta.kcards.playingcards.suits.*
import agrfesta.kcards.playingcards.suits.FrenchSeed.*
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("FLUSH DRAW tests")
class FlushDrawTest {

    @Test
    @DisplayName("Create from not sorted ranks -> ranks are sorted")
    fun createFromUnorderedRanksResultInOrderedRanks() {
        val fd = FlushDraw(JACK,TWO,KING,ACE, CLUBS)
        assertThat(fd.ranks).containsExactly(ACE, KING, JACK, TWO)
    }

    @Test
    @DisplayName("Two have the same Rank -> raises an Exception")
    fun twoHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushDraw(JACK,ACE,KING,ACE, CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: JACK,ACE,KING,ACE")
    }
    @Test
    @DisplayName("Three have the same Rank -> raises an Exception")
    fun threeHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushDraw(JACK,ACE,ACE,ACE, CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: JACK,ACE,ACE,ACE")
    }
    @Test
    @DisplayName("All have the same Rank -> raises an Exception")
    fun allHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushDraw(ACE,ACE,ACE,ACE, CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: ACE,ACE,ACE,ACE")
    }

    @TestFactory
    @DisplayName("equals tests")
    fun equalsTests() = listOf(
            Triple(FlushDraw(TEN,QUEEN,ACE,SIX, HEARTS),FlushDraw(SIX,TEN,ACE,QUEEN, HEARTS), true),
            Triple(FlushDraw(TEN,QUEEN,ACE,SIX, HEARTS),aDraw, false),
            Triple(FlushDraw(TEN,QUEEN,ACE,SIX, HEARTS),FlushDraw(TEN,QUEEN,ACE,SIX, DIAMONDS), false),
            Triple(FlushDraw(TEN,QUEEN,ACE,SIX, HEARTS),FlushDraw(TEN,QUEEN,ACE,FIVE, HEARTS), false),
            Triple(FlushDraw(TEN,QUEEN,ACE,SIX, HEARTS),FlushDraw(TEN,QUEEN,KING,FIVE, DIAMONDS), false),
            Triple(FlushDraw(TEN,QUEEN,ACE,SIX, HEARTS),FlushDraw(TEN,TWO,KING,FIVE, DIAMONDS), false),
            Triple(FlushDraw(TEN,QUEEN,ACE,SIX, HEARTS),FlushDraw(JACK,TWO,KING,FIVE, DIAMONDS), false)
    ).map {
        DynamicTest.dynamicTest(
                "${it.first} ${if (it.third) '=' else '!'}= ${it.second}"
        ) {
            if (it.third) assertThat(it.first).isEqualTo(it.second)
            else assertThat(it.first).isNotEqualTo(it.second)
        }
    }
}