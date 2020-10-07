package agrfesta.k.cards.texasholdem.tournaments

import agrfesta.k.cards.texasholdem.rules.gameplay.Game
import agrfesta.k.cards.texasholdem.rules.gameplay.GamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus
import agrfesta.k.cards.texasholdem.rules.gameplay.Position
import agrfesta.k.cards.texasholdem.rules.gameplay.Table
import agrfesta.k.cards.texasholdem.rules.gameplay.aStrategy
import agrfesta.k.cards.texasholdem.rules.gameplay.get
import agrfesta.k.cards.texasholdem.rules.gameplay.isSittingOn
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
        val mockedGames = listOf< (List<GamePlayer>) -> Game >(
                { mockk(relaxed = true) },
                { mockk(relaxed = true) },
                { mockk(relaxed = true) },
                { mockk(relaxed = true) },
                { players ->
                    val game = mockk<Game>()
                    every { game.play() } answers {
                        players.get(poly)?.stack = 0
                        players.get(jane)?.stack = 0
                        players.get(dave)?.stack = 0
                    }
                    game
                }
        )
        val tables = mutableListOf<Table>()

        val result = tournamentBuilder()
                .subscriptions(poly, jane, alex, dave)
                .buttonProvider { 2 } // button of first game in position 2
                .gameProvider { igp, table, _ ->
                    assertThat(igp === payments).isTrue()
                    tables.add(table)
                    mockedGames[counter++].invoke(table.players)
                }
                .build(2000, payments)
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
        val mockedGames = listOf< (List<GamePlayer>) -> Game >(
                { players ->
                    val game = mockk<Game>()
                    every { game.play() } answers {
                        players.get(alex)?.stack = 0
                    }
                    game
                },
                { players ->
                    val game = mockk<Game>()
                    every { game.play() } answers {
                        players.get(poly)?.stack = 0
                    }
                    game
                }
        )
        val tables = mutableListOf<Table>()

        val result = tournamentBuilder()
                .subscriptions(poly, jane, alex)
                .buttonProvider { 2 } // button of first game in position 2
                .gameProvider { igp, table, _ ->
                    assertThat(igp === payments).isTrue()
                    tables.add(table)
                    mockedGames[counter++].invoke(table.players)
                }
                .build(2000, payments)
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
        val mockedGames = listOf< (List<GamePlayer>) -> Game >(
                { players ->
                    val game = mockk<Game>()
                    every { game.play() } answers {
                        players.get(alex)?.stack = 1500
                        players.get(jane)?.stack = 1000
                    }
                    game
                },
                { players ->
                    val game = mockk<Game>()
                    every { game.play() } answers {
                        players.get(alex)?.stack = 0
                        players.get(jane)?.stack = 0
                    }
                    game
                }
        )
        val tables = mutableListOf<Table>()

        val result = tournamentBuilder()
                .subscriptions(poly, jane, alex)
                .buttonProvider { 2 } // button of first game in position 2
                .gameProvider { igp, table, _ ->
                    assertThat(igp === payments).isTrue()
                    tables.add(table)
                    mockedGames[counter++].invoke(table.players)
                }
                .build(2000, payments)
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
        val mockedGames = listOf< (List<GamePlayer>) -> Game >(
                { players ->
                    val game = mockk<Game>()
                    every { game.play() } answers {
                        players.get(alex)?.stack = 1000
                        players.get(jane)?.stack = 1000
                    }
                    game
                },
                { players ->
                    val game = mockk<Game>()
                    every { game.play() } answers {
                        players.get(alex)?.stack = 0
                        players.get(jane)?.stack = 0
                    }
                    game
                }
        )
        val tables = mutableListOf<Table>()

        val result = tournamentBuilder()
                .subscriptions(poly, jane, alex)
                .buttonProvider { 2 } // button of first game in position 2
                .gameProvider { igp, table, _ ->
                    assertThat(igp === payments).isTrue()
                    tables.add(table)
                    mockedGames[counter++].invoke(table.players)
                }
                .build(2000, payments)
                .play()

        assertThat(result.size).isEqualTo(2)
        assertThat(result).theWinnerIs(poly)
        assertThat(result[1]).containsOnly(alex,jane) // seconds
    }

    @Test
    @DisplayName("Every game reset the initial status of the players")
    fun everyGameResetTheInitialPlayersStatus() {
        val alex = Player("Alex", aStrategy())
        val poly = Player("Poly", aStrategy())
        val jane = Player("Jane", aStrategy())
        var counter = 0
        val payments = mockk<IncreasingGamePayments>(relaxed = true)
        val mockedGames = listOf< (List<GamePlayer>) -> Game >(
                { players ->
                    val game = mockk<Game>()
                    every { game.play() } answers {
                        players.get(alex)?.status = PlayerStatus.ALL_IN
                        players.get(jane)?.status = PlayerStatus.FOLD
                        players.get(poly)?.status = PlayerStatus.RAISE
                    }
                    game
                },
                { players ->
                    val game = mockk<Game>()
                    every { game.play() } answers {
                        players.get(alex)?.stack = 0
                        players.get(jane)?.stack = 0
                    }
                    game
                }
        )
        val tables = mutableListOf<Table>()

        val result = tournamentBuilder()
                .subscriptions(poly, jane, alex)
                .buttonProvider { 2 } // button of first game in position 2
                .gameProvider { igp, table, _ ->
                    assertThat(igp === payments).isTrue()
                    tables.add(table)
                    mockedGames[counter++].invoke(table.players)
                }
                .build(2000, payments)
                .play()

        assertThat(tables[0].players).extracting { it.status }
                .containsOnly(PlayerStatus.NONE,PlayerStatus.NONE,PlayerStatus.NONE)
        assertThat(tables[1].players).extracting { it.status }
                .containsOnly(PlayerStatus.NONE,PlayerStatus.NONE,PlayerStatus.NONE)

        assertThat(result.size).isEqualTo(2)
        assertThat(result).theWinnerIs(poly)
        assertThat(result[1]).containsOnly(alex,jane) // seconds
    }
}
