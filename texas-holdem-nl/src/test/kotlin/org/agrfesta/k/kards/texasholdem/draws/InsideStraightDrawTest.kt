package org.agrfesta.k.kards.texasholdem.draws

import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.agrfesta.k.cards.playingcards.suits.ACE
import org.agrfesta.k.cards.playingcards.suits.EIGHT
import org.agrfesta.k.cards.playingcards.suits.FIVE
import org.agrfesta.k.cards.playingcards.suits.FOUR
import org.agrfesta.k.cards.playingcards.suits.JACK
import org.agrfesta.k.cards.playingcards.suits.KING
import org.agrfesta.k.cards.playingcards.suits.NINE
import org.agrfesta.k.cards.playingcards.suits.QUEEN
import org.agrfesta.k.cards.playingcards.suits.SEVEN
import org.agrfesta.k.cards.playingcards.suits.SIX
import org.agrfesta.k.cards.playingcards.suits.TEN
import org.agrfesta.k.cards.playingcards.suits.THREE
import org.agrfesta.k.cards.playingcards.suits.TWO
import org.agrfesta.k.kards.texasholdem.createDynamicTest
import org.agrfesta.k.kards.texasholdem.isEqualTo
import org.agrfesta.k.kards.texasholdem.isNotEqualTo
import org.agrfesta.k.kards.texasholdem.willAssertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("INSIDE STRAIGHT DRAW tests")
class InsideStraightDrawTest {

    @Test
    @DisplayName("Inside Straight Draw top is 4 -> raises an Exception")
    fun insideStraightDrawTopIsFourRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(FOUR, TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Inside Straight Draw top is 5, top: 4")
    }
    @Test
    @DisplayName("Inside Straight Draw top is 3 -> raises an Exception")
    fun insideStraightDrawTopIsThreeRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(THREE, TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Inside Straight Draw top is 5, top: 3")
    }
    @Test
    @DisplayName("Inside Straight Draw top is 2-> raises an Exception")
    fun insideStraightDrawTopIsTwoRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(TWO, TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Inside Straight Draw top is 5, top: 2")
    }

    @Test
    @DisplayName("Inside Straight Draw top is 6 and missing 6 -> raises an Exception")
    fun insideStraightDrawTopIsSixAndMissingSixRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(SIX, SIX)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The missing Rank can't be greater than or equal to top: missing=6, top=6")
    }
    @Test
    @DisplayName("Inside Straight Draw top is 6 and missing 7-> raises an Exception")
    fun insideStraightDrawTopIsSixAndMissingSevenRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(SIX, SEVEN)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The missing Rank can't be greater than or equal to top: missing=7, top=6")
    }

    @Test
    @DisplayName("Inside Straight Draw top is 5 and missing 6 -> raises an Exception")
    fun insideStraightDrawTopIsFiveAndMissingSixRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(FIVE, SIX)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The missing Rank can't be greater than top: missing=6, top=5")
    }

    @Test
    @DisplayName("Inside Straight Draw top is Kand missing 9 -> raises an Exception")
    fun insideStraightDrawTopIsKingAndMissingNineRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(KING, NINE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The missing Rank can't be lesser than or equal to tail: missing=9, tail=9")
    }
    @Test
    @DisplayName("Inside Straight Draw top is Kand missing 8 -> raises an Exception")
    fun insideStraightDrawTopIsKingAndMissingEightRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(KING, EIGHT)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The missing Rank can't be lesser than or equal to tail: missing=8, tail=9")
    }
    @Test
    @DisplayName("Inside Straight Draw top is Aand missing 9 -> raises an Exception")
    fun insideStraightDrawTopIsAceAndMissingNineRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(ACE, NINE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The missing Rank can't be lesser than tail: missing=9, tail=T")
    }

    @TestFactory
    @DisplayName("Equals tests")
    fun equalsTests() = listOf(
            willAssertThat(InsideStraightDraw(QUEEN, TEN))
                    .isEqualTo(InsideStraightDraw(QUEEN, TEN)),
            willAssertThat(InsideStraightDraw(ACE, TEN) as Draw)
                    .isNotEqualTo(aDraw),
            willAssertThat(InsideStraightDraw(ACE, QUEEN))
                    .isNotEqualTo(InsideStraightDraw(ACE, KING)),
            willAssertThat(InsideStraightDraw(TEN, NINE))
                    .isNotEqualTo(InsideStraightDraw(JACK, NINE)),
            willAssertThat(InsideStraightDraw(SEVEN, SIX))
                    .isNotEqualTo(InsideStraightDraw(JACK, NINE))
    ).map { createDynamicTest(it) }
}
