package agrfesta.k.cards.texasholdem.draws

import agrfesta.kcards.playingcards.suits.*
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

@DisplayName("INSIDE STRAIGHT DRAW tests")
class InsideStraightDrawTest {

    @Test
    @DisplayName("Inside Straight Draw top is THREE -> raises an Exception")
    fun insideStraightDrawTopIsThreeRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(THREE, TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Inside Straight Draw top is FOUR, top: THREE")
    }
    @Test
    @DisplayName("Inside Straight Draw top is TWO -> raises an Exception")
    fun insideStraightDrawTopIsTwoRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(TWO, TWO)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The minimum Inside Straight Draw top is FOUR, top: TWO")
    }

    @Test
    @DisplayName("Inside Straight Draw top is FOUR and missing THREE -> raises an Exception")
    fun insideStraightDrawTopIsFourAndMissingThreeRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(FOUR, THREE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("If the Inside Straight Draw top is FOUR the missing have to be FIVE, missing: THREE")
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
    @Test
    @DisplayName("Inside Straight Draw top is ACE and missing NINE -> raises an Exception")
    fun insideStraightDrawTopIsAceAndMissingNineRaisesAnException() {
        val failure = assertThat {
            InsideStraightDraw(ACE, NINE)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("The missing Rank can't be lesser than tail: missing=NINE, tail=TEN")
    }

    @TestFactory
    @DisplayName("Equals tests")
    fun equalsTests() = listOf(
            Triple(InsideStraightDraw(QUEEN, TEN),InsideStraightDraw(QUEEN, TEN), true),
            Triple(InsideStraightDraw(ACE, TEN),aDraw, false),
            Triple(InsideStraightDraw(ACE, QUEEN),InsideStraightDraw(ACE, KING), false),
            Triple(InsideStraightDraw(TEN, NINE),InsideStraightDraw(JACK, NINE), false),
            Triple(InsideStraightDraw(SEVEN, SIX),InsideStraightDraw(JACK, NINE), false)
    ).map {
        DynamicTest.dynamicTest(
                "${it.first} ${if (it.third) '=' else '!'}= ${it.second}"
        ) {
            if (it.third) assertThat(it.first).isEqualTo(it.second)
            else assertThat(it.first).isNotEqualTo(it.second)
        }
    }
}