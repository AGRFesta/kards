package agrfesta.k.cards.texasholdem.draws

import agrfesta.kcards.playingcards.suits.THREE
import agrfesta.kcards.playingcards.suits.TWO
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("OUTSIDE STRAIGHT DRAW tests")
class OutsideStraightDrawTest {

    @Test
    @DisplayName("Outside Straight Draw top is THREE -> raises an Exception")
    fun outsideStraightDrawTopIsThreeRaisesAnException() {
        val failure = assertThat {
            OutsideStraightDraw(THREE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Outside Straight Draw top is FOUR, top: THREE")
    }
    @Test
    @DisplayName("Outside Straight Draw top is TWO -> raises an Exception")
    fun outsideStraightDrawTopIsTwoRaisesAnException() {
        val failure = assertThat {
            OutsideStraightDraw(TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Outside Straight Draw top is FOUR, top: TWO")
    }

}