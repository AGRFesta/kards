package org.agrfesta.k.kards.texasholdem.tournaments

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.extracting
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isTrue
import io.mockk.every
import io.mockk.mockk
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Game
import org.agrfesta.k.kards.texasholdem.rules.gameplay.InGamePlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStack
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Position
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Table
import org.agrfesta.k.kards.texasholdem.rules.gameplay.aPlayerCardsSet
import org.agrfesta.k.kards.texasholdem.rules.gameplay.alex
import org.agrfesta.k.kards.texasholdem.rules.gameplay.anIncreasingGamePayments
import org.agrfesta.k.kards.texasholdem.rules.gameplay.dave
import org.agrfesta.k.kards.texasholdem.rules.gameplay.isSittingOn
import org.agrfesta.k.kards.texasholdem.rules.gameplay.jane
import org.agrfesta.k.kards.texasholdem.rules.gameplay.owns
import org.agrfesta.k.kards.texasholdem.rules.gameplay.poly
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Tournament tests")
class TournamentImplTest {

    @Test
    @DisplayName("Alex start a four players tournament from the Button, the second game from SB, third from BB, " +
            "fourth from UTG and final again from Button.")
    fun tournamentStory000() {
        var counter = 0
        val payments = mockk<IncreasingGamePayments>(relaxed = true)
        val mockedGames = listOf(
                aMockGameWithResult(poly owns 100u, jane owns 100u, alex owns 100u, dave owns 100u),
                aMockGameWithResult(poly owns 100u, jane owns 100u, alex owns 100u, dave owns 100u),
                aMockGameWithResult(poly owns 100u, jane owns 100u, alex owns 100u, dave owns 100u),
                aMockGameWithResult(poly owns 100u, jane owns 100u, alex owns 100u, dave owns 100u),
                aMockGameWithResult(poly owns 0u, jane owns 0u, alex owns 100u, dave owns 0u)
        )
        val tables = mutableListOf<Table<InGamePlayer>>()

        val result = TournamentImpl(
            descriptor = TournamentDescriptorImpl(2000u, payments),
            subscriptions = setOf(poly, jane, alex, dave),
            buttonProvider = { 2u }, // button of first game in position 2
            gameProvider = { igp, table, _ ->
                val inGameTable = table.map { InGamePlayer(it.player, it.stack, aPlayerCardsSet()) }
                assertThat(igp === payments).isTrue()
                tables.add(inGameTable)
                mockedGames[counter++]
            }
        ).play()

        assertThat(alex).isSittingOn(tables[0], Position.BUTTON)
        assertThat(alex).isSittingOn(tables[1], Position.SMALL_BLIND)
        assertThat(alex).isSittingOn(tables[2], Position.BIG_BLIND)
        assertThat(alex).isSittingOn(tables[3], Position.UNDER_THE_GUN)
        assertThat(alex).isSittingOn(tables[4], Position.BUTTON)
        assertThat(result.size).isEqualTo(2)
        assertThat(result).theWinnerIs(alex)
        assertThat(result[1]).containsOnly(dave,poly,jane) // seconds
    }

    @Test
    @DisplayName("In a three players tournament Alex goes all-in an lose, is the first eliminated player and won't " +
            "participate to remaining games.")
    fun tournamentStory001() {
        var counter = 0
        val payments = mockk<IncreasingGamePayments>(relaxed = true)
        val mockedGames = listOf(
                aMockGameWithResult(poly owns 100u, jane owns 100u, alex owns 0u),
                aMockGameWithResult(poly owns 0u, jane owns 100u)
        )
        val tables = mutableListOf<Table<InGamePlayer>>()

        val result = TournamentImpl(
            descriptor = TournamentDescriptorImpl(2000u, payments),
            subscriptions = setOf(poly, jane, alex),
            buttonProvider = { 2u }, // button of first game in position 2
            gameProvider = { igp, table, _ ->
                val inGameTable = table.map { InGamePlayer(it.player, it.stack, aPlayerCardsSet()) }
                assertThat(igp === payments).isTrue()
                tables.add(inGameTable)
                mockedGames[counter++]
            }
        ).play()

        assertThat(tables[0].players).extracting { it.player }.containsOnly(alex,jane,poly)
        assertThat(tables[1].players).extracting { it.player }.containsOnly(jane,poly)
        assertThat(result.size).isEqualTo(3)
        assertThat(result).theWinnerIs(jane)
        assertThat(result[1]).containsOnly(poly) // second
        assertThat(result[2]).containsOnly(alex) // third
    }

    @Test
    @DisplayName("In a three players tournament Alex and Jane go all-in with an initial stack of 1500 and 1000, they " +
            "both lose and are eliminated, Poly win the tournament, Alex second and Jane third.")
    fun tournamentStory002() {
        var counter = 0
        val payments = mockk<IncreasingGamePayments>(relaxed = true)
        val mockedGames = listOf(
                aMockGameWithResult(poly owns 100u, jane owns 1000u, alex owns 1500u),
                aMockGameWithResult(poly owns 100u, jane owns 0u, alex owns 0u)
        )
        val tables = mutableListOf<Table<InGamePlayer>>()

        val result = TournamentImpl(
            descriptor = TournamentDescriptorImpl(2000u, payments),
            subscriptions = setOf(poly, jane, alex),
            buttonProvider = { 2u }, // button of first game in position 2
            gameProvider = { igp, table, _ ->
                val inGameTable = table.map { InGamePlayer(it.player, it.stack, aPlayerCardsSet()) }
                assertThat(igp === payments).isTrue()
                tables.add(inGameTable)
                mockedGames[counter++]
            }
        ).play()

        assertThat(result.size).isEqualTo(3)
        assertThat(result).theWinnerIs(poly)
        assertThat(result[1]).containsOnly(alex) // second
        assertThat(result[2]).containsOnly(jane) // third
    }

    @Test
    @DisplayName("In a three players tournament Alex and Jane go all-in with an initial stack of 1000 and 1000, they " +
            "both lose and are eliminated, Poly win the tournament, Alex and Jane second.")
    fun tournamentStory003() {
        var counter = 0
        val payments = mockk<IncreasingGamePayments>(relaxed = true)
        val mockedGames = listOf(
                aMockGameWithResult(poly owns 100u, jane owns 1000u, alex owns 1000u),
                aMockGameWithResult(poly owns 100u, jane owns 0u, alex owns 0u)
        )
        val tables = mutableListOf<Table<InGamePlayer>>()

        val result = TournamentImpl(
            descriptor = TournamentDescriptorImpl(2000u, payments),
            subscriptions = setOf(poly, jane, alex),
            buttonProvider = { 2u }, // button of first game in position 2
            gameProvider = { igp, table, _ ->
                val inGameTable = table.map { InGamePlayer(it.player, it.stack, aPlayerCardsSet()) }
                assertThat(igp === payments).isTrue()
                tables.add(inGameTable)
                mockedGames[counter++]
            }
        ).play()

        assertThat(result.size).isEqualTo(2)
        assertThat(result).theWinnerIs(poly)
        assertThat(result[1]).containsOnly(alex,jane) // seconds
    }

    @Test
    @DisplayName("constructor(): a Tournament with no subscribers -> raises an Exception")
    fun constructor_aTournamentWithNoSubscribers_raisesAnException() {
        val failure = assertThat {
            TournamentImpl(
                descriptor = TournamentDescriptorImpl(2000u, anIncreasingGamePayments()),
                subscriptions = emptySet() )
        }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Unable to create a tournament with zero players!")
    }

    @Test
    @DisplayName("constructor(): a Tournament with a single subscriber -> raises an Exception")
    fun constructor_aTournamentWithASingleSubscriber_raisesAnException() {
        val failure = assertThat {
            TournamentImpl(
                descriptor = TournamentDescriptorImpl(2000u, anIncreasingGamePayments()),
                subscriptions = setOf(alex) )
        }.isFailure()
        failure.hasClass(IllegalStateException::class)
        failure.hasMessage("Unable to create a tournament with only one player!")
    }

}

private fun aMockGameWithResult(vararg elements: PlayerStack): Game {
    val game = mockk<Game>()
    every { game.play() } answers { elements.toList() }
    return game
}
