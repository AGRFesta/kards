package agrfesta.k.cards.texasholdem.draws

import agrfesta.kcards.playingcards.suits.*
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("INSIDE STRAIGHT DRAW tests")
class InsideStraightDrawTest {

    @Test
    @DisplayName("Inside Straight Draw top is FOUR -> raises an Exception")
    fun insideStraightDrawTopIsFourRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(FOUR, THREE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Inside Straight Draw top is FIVE, top: FOUR")
    }
    @Test
    @DisplayName("Inside Straight Draw top is THREE -> raises an Exception")
    fun insideStraightDrawTopIsThreeRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(THREE, TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Inside Straight Draw top is FIVE, top: THREE")
    }
    @Test
    @DisplayName("Inside Straight Draw top is TWO -> raises an Exception")
    fun insideStraightDrawTopIsTwoRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(TWO, TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Inside Straight Draw top is FIVE, top: TWO")
    }

    @Test
    @DisplayName("Inside Straight Draw top is SIX and missing SIX -> raises an Exception")
    fun insideStraightDrawTopIsSixAndMissingSixRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(SIX, SIX)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The missing Rank can't be greater than or equal to top: missing=SIX, top=SIX")
    }
    @Test
    @DisplayName("Inside Straight Draw top is SIX and missing SEVEN -> raises an Exception")
    fun insideStraightDrawTopIsSixAndMissingSevenRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(SIX, SEVEN)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The missing Rank can't be greater than or equal to top: missing=SEVEN, top=SIX")
    }

    @Test
    @DisplayName("Inside Straight Draw top is KING and missing NINE -> raises an Exception")
    fun insideStraightDrawTopIsKingAndMissingNineRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(KING, NINE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The missing Rank can't be lesser than or equal to tail: missing=NINE, tail=NINE")
    }
    @Test
    @DisplayName("Inside Straight Draw top is KING and missing EIGHT -> raises an Exception")
    fun insideStraightDrawTopIsKingAndMissingEightRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(KING, EIGHT)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The missing Rank can't be lesser than or equal to tail: missing=EIGHT, tail=NINE")
    }
}