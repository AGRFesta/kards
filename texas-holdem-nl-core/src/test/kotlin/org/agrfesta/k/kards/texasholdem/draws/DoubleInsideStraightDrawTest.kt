package org.agrfesta.k.kards.texasholdem.draws

import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
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

@DisplayName("DOUBLE INSIDE STRAIGHT DRAW tests")
class DoubleInsideStraightDrawTest {

    @Test
    @DisplayName("Double Inside Straight Draw inner top is 4 -> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsFourRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(FOUR, SIX)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Double Inside Straight Draw inner top is 5, inner top: 4")
    }
    @Test
    @DisplayName("Double Inside Straight Draw inner top is 3-> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsThreeRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(THREE, FIVE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Double Inside Straight Draw inner top is 5, inner top: 3")
    }
    @Test
    @DisplayName("Double Inside Straight Draw inner top is 2-> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsTwoRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(TWO, FOUR)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Double Inside Straight Draw inner top is 5, inner top: 2")
    }

    @Test
    @DisplayName("Double Inside Straight Draw inner top is 6 and potential T-> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsSixAndPotentialTenRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(SIX, TEN)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("If the inner top is 6 the potential top have to be 8 or 9 but is T")
    }
    @Test
    @DisplayName("Double Inside Straight Draw inner top is J and potential 5 -> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsJackAndPotentialFiveRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(JACK, FIVE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("If the inner top is J the potential top have to be K or A but is 5")
    }
    @Test
    @DisplayName("Double Inside Straight Draw inner top is Tand potential J -> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsTenAndPotentialJackRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(TEN, JACK)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("If the inner top is T the potential top have to be Q or K but is J")
    }

    @TestFactory
    @DisplayName("equals tests")
    fun equalsTests() = listOf(
            willAssertThat(DoubleInsideStraightDraw(TEN, QUEEN))
                    .isEqualTo(DoubleInsideStraightDraw(TEN, QUEEN)),
            willAssertThat(DoubleInsideStraightDraw(TEN, QUEEN) as Draw)
                    .isNotEqualTo(aDraw),
            willAssertThat(DoubleInsideStraightDraw(TEN, QUEEN))
                    .isNotEqualTo(DoubleInsideStraightDraw(TEN, KING)),
            willAssertThat(DoubleInsideStraightDraw(TEN, QUEEN))
                    .isNotEqualTo(DoubleInsideStraightDraw(SEVEN, NINE))
    ).map { createDynamicTest(it) }
}
