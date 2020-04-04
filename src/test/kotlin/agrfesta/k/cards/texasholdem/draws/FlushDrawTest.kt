package agrfesta.k.cards.texasholdem.draws

import agrfesta.kcards.playingcards.suits.*
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("FLUSH DRAW tests")
class FlushDrawTest {

    @Test
    @DisplayName("Create from not sorted ranks -> ranks are sorted")
    fun createFromUnorderedRanksResultInOrderedRanks() {
        val fd = FlushDraw(JACK, TWO, KING, ACE, FrenchSeed.CLUBS)
        assertThat(fd.ranks).containsExactly(ACE, KING, JACK, TWO)
    }

    @Test
    @DisplayName("Two have the same Rank -> raises an Exception")
    fun twoHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushDraw(JACK, ACE, KING, ACE, FrenchSeed.CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: JACK,ACE,KING,ACE")
    }
    @Test
    @DisplayName("Three have the same Rank -> raises an Exception")
    fun threeHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushDraw(JACK, ACE, ACE, ACE, FrenchSeed.CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: JACK,ACE,ACE,ACE")
    }
    @Test
    @DisplayName("All have the same Rank -> raises an Exception")
    fun allHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushDraw(ACE, ACE, ACE, ACE, FrenchSeed.CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: ACE,ACE,ACE,ACE")
    }
}