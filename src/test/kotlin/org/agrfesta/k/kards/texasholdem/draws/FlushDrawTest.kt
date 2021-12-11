package org.agrfesta.k.kards.texasholdem.draws

import org.agrfesta.k.kards.texasholdem.createDynamicTest
import org.agrfesta.k.kards.texasholdem.isEqualTo
import org.agrfesta.k.kards.texasholdem.isNotEqualTo
import org.agrfesta.k.kards.texasholdem.willAssertThat
import agrfesta.k.cards.playingcards.suits.*
import agrfesta.k.cards.playingcards.suits.FrenchSeed.*
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
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
            willAssertThat(FlushDraw(TEN,QUEEN,ACE,SIX, HEARTS))
                    .isEqualTo(FlushDraw(SIX,TEN,ACE,QUEEN, HEARTS)),
            willAssertThat(FlushDraw(TEN,QUEEN,ACE,SIX, HEARTS) as Draw)
                    .isNotEqualTo(aDraw),
            willAssertThat(FlushDraw(TEN,QUEEN,ACE,SIX, HEARTS))
                    .isNotEqualTo(FlushDraw(TEN,QUEEN,ACE,SIX, DIAMONDS)),
            willAssertThat(FlushDraw(TEN,QUEEN,ACE,SIX, HEARTS))
                    .isNotEqualTo(FlushDraw(TEN,QUEEN,ACE,FIVE, HEARTS)),
            willAssertThat(FlushDraw(TEN,QUEEN,ACE,SIX, HEARTS))
                    .isNotEqualTo(FlushDraw(TEN,QUEEN,KING,FIVE, DIAMONDS)),
            willAssertThat(FlushDraw(TEN,QUEEN,ACE,SIX, HEARTS))
                    .isNotEqualTo(FlushDraw(TEN,TWO,KING,FIVE, DIAMONDS)),
            willAssertThat(FlushDraw(TEN,QUEEN,ACE,SIX, HEARTS))
                    .isNotEqualTo(FlushDraw(JACK,TWO,KING,FIVE, DIAMONDS))
    ).map { createDynamicTest(it) }
}
