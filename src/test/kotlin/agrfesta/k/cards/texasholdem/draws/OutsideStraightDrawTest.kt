package agrfesta.k.cards.texasholdem.draws

import agrfesta.k.cards.texasholdem.createDynamicTest
import agrfesta.k.cards.texasholdem.isEqualTo
import agrfesta.k.cards.texasholdem.isNotEqualTo
import agrfesta.k.cards.texasholdem.willAssertThat
import agrfesta.k.cards.playingcards.suits.*
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("OUTSIDE STRAIGHT DRAW tests")
class OutsideStraightDrawTest {

    @Test
    @DisplayName("Outside Straight Draw top is FOUR -> raises an Exception")
    fun outsideStraightDrawTopIsFourRaisesAnException() {
        val failure = assertThat {
            OutsideStraightDraw(FOUR)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Outside Straight Draw top is FIVE, top: FOUR")
    }
    @Test
    @DisplayName("Outside Straight Draw top is THREE -> raises an Exception")
    fun outsideStraightDrawTopIsThreeRaisesAnException() {
        val failure = assertThat {
            OutsideStraightDraw(THREE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Outside Straight Draw top is FIVE, top: THREE")
    }
    @Test
    @DisplayName("Outside Straight Draw top is TWO -> raises an Exception")
    fun outsideStraightDrawTopIsTwoRaisesAnException() {
        val failure = assertThat {
            OutsideStraightDraw(TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Outside Straight Draw top is FIVE, top: TWO")
    }
    @Test
    @DisplayName("Outside Straight Draw top is ACE -> raises an Exception")
    fun outsideStraightDrawTopIsAceRaisesAnException() {
        val failure = assertThat {
            OutsideStraightDraw(ACE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The maximum Outside Straight Draw top is KING, top: ACE")
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
