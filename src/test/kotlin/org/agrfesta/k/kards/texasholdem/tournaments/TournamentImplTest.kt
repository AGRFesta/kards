package org.agrfesta.k.kards.texasholdem.tournaments

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.extracting
import assertk.assertions.isEqualTo
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
import org.agrfesta.k.kards.texasholdem.rules.gameplay.dave
import org.agrfesta.k.kards.texasholdem.rules.gameplay.isSittingOn
import org.agrfesta.k.kards.texasholdem.rules.gameplay.jane
import org.agrfesta.k.kards.texasholdem.rules.gameplay.owns
import org.agrfesta.k.kards.texasholdem.rules.gameplay.poly
import org.agrfesta.k.kards.texasholdem.tournaments.TournamentBuilder.Companion.buildingTournament
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
                aMockGameWithResult(poly owns 100, jane owns 100, alex owns 100, dave owns 100),
                aMockGameWithResult(poly owns 100, jane owns 100, alex owns 100, dave owns 100),
                aMockGameWithResult(poly owns 100, jane owns 100, alex owns 100, dave owns 100),
                aMockGameWithResult(poly owns 100, jane owns 100, alex owns 100, dave owns 100),
                aMockGameWithResult(poly owns 0, jane owns 0, alex owns 100, dave owns 0)
        )
        val tables = mutableListOf<Table<InGamePlayer>>()

        val result = buildingTournament()
                .withAnInitialStackOf(2000)
                .withPayments(payments)
                .withSubscribers(poly, jane, alex, dave)
                .withButtonProvider { 2 } // button of first game in position 2
                .withGameProvider { igp, table, _ ->
                    val inGameTable = table.map { InGamePlayer(it.player, it.stack, aPlayerCardsSet()) }
                    assertThat(igp === payments).isTrue()
                    tables.add(inGameTable)
                    mockedGames[counter++]
                }
                .build()
                .play()

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
                aMockGameWithResult(poly owns 100, jane owns 100, alex owns 0),
                aMockGameWithResult(poly owns 0, jane owns 100)
        )
        val tables = mutableListOf<Table<InGamePlayer>>()

        val result = buildingTournament()
                .withAnInitialStackOf(2000)
                .withPayments(payments)
                .withSubscribers(poly, jane, alex)
                .withButtonProvider { 2 } // button of first game in position 2
                .withGameProvider { igp, table, _ ->
                    val inGameTable = table.map { InGamePlayer(it.player, it.stack, aPlayerCardsSet()) }
                    assertThat(igp === payments).isTrue()
                    tables.add(inGameTable)
                    mockedGames[counter++]
                }
                .build()
                .play()

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
                aMockGameWithResult(poly owns 100, jane owns 1000, alex owns 1500),
                aMockGameWithResult(poly owns 100, jane owns 0, alex owns 0)
        )
        val tables = mutableListOf<Table<InGamePlayer>>()

        val result = buildingTournament()
                .withAnInitialStackOf(2000)
                .withPayments(payments)
                .withSubscribers(poly, jane, alex)
                .withButtonProvider { 2 } // button of first game in position 2
                .withGameProvider { igp, table, _ ->
                    val inGameTable = table.map { InGamePlayer(it.player, it.stack, aPlayerCardsSet()) }
                    assertThat(igp === payments).isTrue()
                    tables.add(inGameTable)
                    mockedGames[counter++]
                }
                .build()
                .play()

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
                aMockGameWithResult(poly owns 100, jane owns 1000, alex owns 1000),
                aMockGameWithResult(poly owns 100, jane owns 0, alex owns 0)
        )
        val tables = mutableListOf<Table<InGamePlayer>>()

        val result = buildingTournament()
                .withAnInitialStackOf(2000)
                .withPayments(payments)
                .withSubscribers(poly, jane, alex)
                .withButtonProvider { 2 } // button of first game in position 2
                .withGameProvider { igp, table, _ ->
                    val inGameTable = table.map { InGamePlayer(it.player, it.stack, aPlayerCardsSet()) }
                    assertThat(igp === payments).isTrue()
                    tables.add(inGameTable)
                    mockedGames[counter++]
                }
                .build()
                .play()

        assertThat(result.size).isEqualTo(2)
        assertThat(result).theWinnerIs(poly)
        assertThat(result[1]).containsOnly(alex,jane) // seconds
    }

}

private fun aMockGameWithResult(vararg elemnts: PlayerStack): Game {
    val game = mockk<Game>()
    every { game.play() } answers { elemnts.toList() }
    return game
}
