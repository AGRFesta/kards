package agrfesta.k.cards.texasholdem.tournaments

import agrfesta.k.cards.texasholdem.rules.gameplay.Game
import agrfesta.k.cards.texasholdem.rules.gameplay.InGamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStack
import agrfesta.k.cards.texasholdem.rules.gameplay.Position
import agrfesta.k.cards.texasholdem.rules.gameplay.Table
import agrfesta.k.cards.texasholdem.rules.gameplay.aPlayerCardsSet
import agrfesta.k.cards.texasholdem.rules.gameplay.aStrategy
import agrfesta.k.cards.texasholdem.rules.gameplay.isSittingOn
import agrfesta.k.cards.texasholdem.tournaments.TournamentBuilder.Companion.buildingTournament
import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.extracting
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Tournament tests")
class TournamentImplTest {

    @Test
    @DisplayName("Alex start a four players tournament from the Button, the second game from SB, third from BB, " +
            "fourth from UTG and final again from Button.")
    fun tournamentStory000() {
        val alex = Player("Alex", aStrategy())
        val poly = Player("Poly", aStrategy())
        val jane = Player("Jane", aStrategy())
        val dave = Player("Dave", aStrategy())
        var counter = 0
        val payments = mockk<IncreasingGamePayments>(relaxed = true)
        val mockedGames = listOf(
                aMockGameWithResult(poly to 100, jane to 100, alex to 100, dave to 100),
                aMockGameWithResult(poly to 100, jane to 100, alex to 100, dave to 100),
                aMockGameWithResult(poly to 100, jane to 100, alex to 100, dave to 100),
                aMockGameWithResult(poly to 100, jane to 100, alex to 100, dave to 100),
                aMockGameWithResult(poly to 0, jane to 0, alex to 100, dave to 0)
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
        val alex = Player("Alex", aStrategy())
        val poly = Player("Poly", aStrategy())
        val jane = Player("Jane", aStrategy())
        var counter = 0
        val payments = mockk<IncreasingGamePayments>(relaxed = true)
        val mockedGames = listOf(
                aMockGameWithResult(poly to 100, jane to 100, alex to 0),
                aMockGameWithResult(poly to 0, jane to 100)
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
        val alex = Player("Alex", aStrategy())
        val poly = Player("Poly", aStrategy())
        val jane = Player("Jane", aStrategy())
        var counter = 0
        val payments = mockk<IncreasingGamePayments>(relaxed = true)
        val mockedGames = listOf(
                aMockGameWithResult(poly to 100, jane to 1000, alex to 1500),
                aMockGameWithResult(poly to 100, jane to 0, alex to 0)
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
        val alex = Player("Alex", aStrategy())
        val poly = Player("Poly", aStrategy())
        val jane = Player("Jane", aStrategy())
        var counter = 0
        val payments = mockk<IncreasingGamePayments>(relaxed = true)
        val mockedGames = listOf(
                aMockGameWithResult(poly to 100, jane to 1000, alex to 1000),
                aMockGameWithResult(poly to 100, jane to 0, alex to 0)
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

private fun aMockGameWithResult(vararg pairs: Pair<Player,Int>): Game {
    val game = mockk<Game>()
    every { game.play() } answers {
        pairs.map { PlayerStack(it.first, it.second) }.toList()
    }
    return game
}
