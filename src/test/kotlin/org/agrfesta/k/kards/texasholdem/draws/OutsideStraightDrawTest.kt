package org.agrfesta.k.kards.texasholdem.draws

import org.agrfesta.k.kards.texasholdem.createDynamicTest
import org.agrfesta.k.kards.texasholdem.isEqualTo
import org.agrfesta.k.kards.texasholdem.isNotEqualTo
import org.agrfesta.k.kards.texasholdem.willAssertThat
import org.agrfesta.k.cards.playingcards.suits.*
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("OUTSIDE STRAIGHT DRAW tests")
class OutsideStraightDrawTest {

    @Test
    @DisplayName("Outside Straight Draw top is 4 -> raises an Exception")
    fun outsideStraightDrawTopIsFourRaisesAnException() {
        val failure = assertThat {
            OutsideStraightDraw(FOUR)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Outside Straight Draw top is 5, top: 4")
    }
    @Test
    @DisplayName("Outside Straight Draw top is 3-> raises an Exception")
    fun outsideStraightDrawTopIsThreeRaisesAnException() {
        val failure = assertThat {
            OutsideStraightDraw(THREE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Outside Straight Draw top is 5, top: 3")
    }
    @Test
    @DisplayName("Outside Straight Draw top is 2-> raises an Exception")
    fun outsideStraightDrawTopIsTwoRaisesAnException() {
        val failure = assertThat {
            OutsideStraightDraw(TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Outside Straight Draw top is 5, top: 2")
    }
    @Test
    @DisplayName("Outside Straight Draw top is A-> raises an Exception")
    fun outsideStraightDrawTopIsAceRaisesAnException() {
        val failure = assertThat {
            OutsideStraightDraw(ACE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The maximum Outside Straight Draw top is K, top: A")
    }

    @TestFactory
    @DisplayName("Equals tests")
    fun equalsTests() = listOf(
            willAssertThat(OutsideStraightDraw(QUEEN))
                    .isEqualTo(OutsideStraightDraw(QUEEN)),
            willAssertThat(OutsideStraightDraw(FIVE) as Draw)
                    .isNotEqualTo(aDraw),
            willAssertThat(OutsideStraightDraw(KING))
                    .isNotEqualTo(OutsideStraightDraw(QUEEN))
    ).map { createDynamicTest(it) }
}
