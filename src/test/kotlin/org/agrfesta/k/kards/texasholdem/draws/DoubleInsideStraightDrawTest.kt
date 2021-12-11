package org.agrfesta.k.kards.texasholdem.draws

import agrfesta.k.cards.texasholdem.*
import agrfesta.k.cards.playingcards.suits.*
import assertk.assertThat
import assertk.assertions.*
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
    @DisplayName("Double Inside Straight Draw inner top is FOUR -> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsFourRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(FOUR, SIX)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Double Inside Straight Draw inner top is FIVE, inner top: FOUR")
    }
    @Test
    @DisplayName("Double Inside Straight Draw inner top is THREE -> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsThreeRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(THREE, FIVE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Double Inside Straight Draw inner top is FIVE, inner top: THREE")
    }
    @Test
    @DisplayName("Double Inside Straight Draw inner top is TWO -> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsTwoRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(TWO, FOUR)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Double Inside Straight Draw inner top is FIVE, inner top: TWO")
    }

    @Test
    @DisplayName("Double Inside Straight Draw inner top is SIX and potential TEN -> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsSixAndPotentialTenRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(SIX, TEN)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("If the inner top is SIX the potential top have to be EIGHT or NINE but is TEN")
    }
    @Test
    @DisplayName("Double Inside Straight Draw inner top is JACK and potential FIVE -> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsJackAndPotentialFiveRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(JACK, FIVE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("If the inner top is JACK the potential top have to be KING or ACE but is FIVE")
    }
    @Test
    @DisplayName("Double Inside Straight Draw inner top is TEN and potential JACK -> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsTenAndPotentialJackRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(TEN, JACK)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("If the inner top is TEN the potential top have to be QUEEN or KING but is JACK")
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
