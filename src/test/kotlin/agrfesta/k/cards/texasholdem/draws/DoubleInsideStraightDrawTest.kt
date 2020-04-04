package agrfesta.k.cards.texasholdem.draws

import agrfesta.kcards.playingcards.suits.*
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("DOUBLE INSIDE STRAIGHT DRAW tests")
class DoubleInsideStraightDrawTest {

    @Test
    @DisplayName("Double Inside Straight Draw inner top is FIVE -> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsFiveRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(FIVE, SEVEN)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Double Inside Straight Draw inner top is SIX, inner top: FIVE")
    }
    @Test
    @DisplayName("Double Inside Straight Draw inner top is FOUR -> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsFourRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(FOUR, SIX)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Double Inside Straight Draw inner top is SIX, inner top: FOUR")
    }
    @Test
    @DisplayName("Double Inside Straight Draw inner top is THREE -> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsThreeRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(THREE, FIVE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Double Inside Straight Draw inner top is SIX, inner top: THREE")
    }
    @Test
    @DisplayName("Double Inside Straight Draw inner top is TWO -> raises an Exception")
    fun doubleInsideStraightDrawInnerTopIsTwoRaisesAnException() {
        val failure = assertThat {
            DoubleInsideStraightDraw(TWO, FOUR)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Double Inside Straight Draw inner top is SIX, inner top: TWO")
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
}