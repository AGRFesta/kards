package org.agrfesta.k.kards.texasholdem.draws

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.agrfesta.k.cards.playingcards.suits.ACE
import org.agrfesta.k.cards.playingcards.suits.FIVE
import org.agrfesta.k.cards.playingcards.suits.FrenchSeed.CLUBS
import org.agrfesta.k.cards.playingcards.suits.FrenchSeed.DIAMONDS
import org.agrfesta.k.cards.playingcards.suits.FrenchSeed.HEARTS
import org.agrfesta.k.cards.playingcards.suits.JACK
import org.agrfesta.k.cards.playingcards.suits.KING
import org.agrfesta.k.cards.playingcards.suits.QUEEN
import org.agrfesta.k.cards.playingcards.suits.SIX
import org.agrfesta.k.cards.playingcards.suits.TEN
import org.agrfesta.k.cards.playingcards.suits.TWO
import org.agrfesta.k.kards.texasholdem.createDynamicTest
import org.agrfesta.k.kards.texasholdem.isEqualTo
import org.agrfesta.k.kards.texasholdem.isNotEqualTo
import org.agrfesta.k.kards.texasholdem.willAssertThat
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
        failure.hasMessage("Multiple with same Rank: J,A,K,A")
    }
    @Test
    @DisplayName("Three have the same Rank -> raises an Exception")
    fun threeHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushDraw(JACK,ACE,ACE,ACE, CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: J,A,A,A")
    }
    @Test
    @DisplayName("All have the same Rank -> raises an Exception")
    fun allHaveTheSameRankRaisesAnException() {
        val failure = assertThat {
            FlushDraw(ACE,ACE,ACE,ACE, CLUBS)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Multiple with same Rank: A,A,A,A")
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
