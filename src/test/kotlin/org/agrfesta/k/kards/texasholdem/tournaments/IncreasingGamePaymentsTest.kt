package org.agrfesta.k.kards.texasholdem.tournaments

import assertk.Assert
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.prop
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Increasing Game Payments tests")
class IncreasingGamePaymentsTest {
    private val level0 = LevelPayments(10,20,null)
    private val level1 = LevelPayments(50,100,10)

    @Test
    @DisplayName("Create it with an empty structure -> raises an exception")
    fun emptyStructureRaisesException() {
        val failure = assertThat {
            IncreasingGamePayments(listOf(), 10)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Unable to create an IncreasingGamePayments from an empty structure")
    }

    @Test
    @DisplayName("Create it with gamesPerLevel equals to 0 -> raises an exception")
    fun gamesPerLevelEqualsToZeroRaisesException() {
        val failure = assertThat {
            IncreasingGamePayments(listOf(level0), 0)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Unable to create an IncreasingGamePayments, gamesPerLevel=0")
    }
    @Test
    @DisplayName("Create it with gamesPerLevel equals to -3 -> raises an exception")
    fun gamesPerLevelEqualsToMinusThreeRaisesException() {
        val failure = assertThat {
            IncreasingGamePayments(listOf(level0), -3)
        }.isFailure()
        failure.hasClass(IllegalArgumentException::class)
        failure.hasMessage("Unable to create an IncreasingGamePayments, gamesPerLevel=-3")
    }

    @Test
    @DisplayName("Payments with two level and 3 games per each: After 3 games -> Returns payments for the second level")
    fun afterThreeGamesReturnsSecondLevelPayments() {
        val payments = IncreasingGamePayments(listOf(level0,level1), 3)
        assertThat(payments).hasLevel(level0)
        payments.nextGame()
        assertThat(payments).hasLevel(level0)
        payments.nextGame()
        assertThat(payments).hasLevel(level0)
        payments.nextGame()
        assertThat(payments).hasLevel(level1)
    }

    private fun Assert<IncreasingGamePayments>.hasLevel(expected: LevelPayments) {
        prop(IncreasingGamePayments::sb).isEqualTo(expected.sb)
        prop(IncreasingGamePayments::bb).isEqualTo(expected.bb)
        prop(IncreasingGamePayments::ante).isEqualTo(expected.ante)
    }
}
