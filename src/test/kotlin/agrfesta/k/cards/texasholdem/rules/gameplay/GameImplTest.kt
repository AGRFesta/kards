package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.texasholdem.playercontext.PlayerAction
import agrfesta.k.cards.texasholdem.rules.gameplay.GameBuilder.Companion.buildingAGame
import assertk.assertThat
import assertk.assertions.containsExactly
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class GameImplTest {

    @Test
    @DisplayName("play(): playing a game -> all players actions are notified to the game's observer")
    fun playTest000() {
        val playerA = Player( "Alex", strategyMock(call()) )
        val playerP = Player( "Poly", strategyMock(call()) )
        val gameObserverMock = mockk<GameObserver>(relaxed = true)
        val table: Table<PlayerStack> = aTableWith(playerA, playerP)
            .map { PlayerStack(it.player, 1000) }
        val game = buildingAGame()
            .withPayments(10, 20)
            .withTable(table)
            .observedBy(gameObserverMock)
            .implementedBy(::GameImpl)
            .build()

        game.play()

        val phases = mutableListOf<GamePhase>()
        verify { gameObserverMock.notifyActions(capture(phases), any()) }
        assertThat(phases).containsExactly(
            GamePhase.PRE_FLOP, GamePhase.FLOP, GamePhase.TURN, GamePhase.RIVER
        )
        val actions = mutableListOf<PlayerAction>()
        verify { gameObserverMock.notifyAction(any(), capture(actions)) }
        assertThat(actions).containsExactly(
            PlayerAction("Alex", call()),
            PlayerAction("Poly", call()),
            PlayerAction("Alex", call()),
            PlayerAction("Poly", call()),
            PlayerAction("Alex", call()),
            PlayerAction("Poly", call()),
            PlayerAction("Alex", call()),
            PlayerAction("Poly", call())
        )
    }

}

