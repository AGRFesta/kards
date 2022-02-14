package org.agrfesta.k.kards.texasholdem.observers

import io.mockk.mockk
import io.mockk.verify
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerAction
import org.agrfesta.k.kards.texasholdem.rules.gameplay.aGameContext
import org.agrfesta.k.kards.texasholdem.rules.gameplay.aGameResult
import org.agrfesta.k.kards.texasholdem.rules.gameplay.aPlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.aShowdownPlayerResult
import org.agrfesta.k.kards.texasholdem.rules.gameplay.call
import org.agrfesta.k.kards.texasholdem.rules.gameplay.does
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class GameBroadcasterTest {
    private val subscriberA = mockk<GameObserver>(relaxed = true)
    private val subscriberB = mockk<GameObserver>(relaxed = true)
    private val subscriberC = mockk<GameObserver>(relaxed = true)

    @Test
    @DisplayName("""notifyWinner(): executing broadcast notifyWinner -> all subscribers are notified""")
    fun notifyWinner_allSubscribersAreNotified() {
        val gameResult = aGameResult()
        val broadcaster = GameBroadcaster()
        broadcaster.subscribe(subscriberA)
        broadcaster.subscribe(subscriberB)
        broadcaster.subscribe(subscriberC)

        broadcaster.notifyWinner(gameResult)

        verify(exactly = 1) { subscriberA.notifyWinner(gameResult) }
        verify(exactly = 1) { subscriberB.notifyWinner(gameResult) }
        verify(exactly = 1) { subscriberC.notifyWinner(gameResult) }
    }

    @Test
    @DisplayName("""notifyStartingPhase(): executing broadcast notifyStartingPhase -> all subscribers are notified""")
    fun notifyStartingPhase_allSubscribersAreNotified() {
        val gameContext = aGameContext()
        val broadcaster = GameBroadcaster()
        broadcaster.subscribe(subscriberA)
        broadcaster.subscribe(subscriberB)
        broadcaster.subscribe(subscriberC)

        broadcaster.notifyStartingPhase(gameContext)

        verify(exactly = 1) { subscriberA.notifyStartingPhase(gameContext) }
        verify(exactly = 1) { subscriberB.notifyStartingPhase(gameContext) }
        verify(exactly = 1) { subscriberC.notifyStartingPhase(gameContext) }
    }

    @Test
    @DisplayName("""notifyResult(): executing broadcast notifyResult -> all subscribers are notified""")
    fun notifyResult_allSubscribersAreNotified() {
        val result: Collection<ShowdownPlayerResult> = listOf(aShowdownPlayerResult())
        val broadcaster = GameBroadcaster()
        broadcaster.subscribe(subscriberA)
        broadcaster.subscribe(subscriberB)
        broadcaster.subscribe(subscriberC)

        broadcaster.notifyResult(result)

        verify(exactly = 1) { subscriberA.notifyResult(result) }
        verify(exactly = 1) { subscriberB.notifyResult(result) }
        verify(exactly = 1) { subscriberC.notifyResult(result) }
    }

    @Test
    @DisplayName("""notifyActions(): executing broadcast notifyActions -> all subscribers are notified""")
    fun notifyActions_allSubscribersAreNotified() {
        val phase = GamePhase.PRE_FLOP
        val actions = emptyList<PlayerAction>()
        val broadcaster = GameBroadcaster()
        broadcaster.subscribe(subscriberA)
        broadcaster.subscribe(subscriberB)
        broadcaster.subscribe(subscriberC)

        broadcaster.notifyActions(phase, actions)

        verify(exactly = 1) { subscriberA.notifyActions(phase, actions) }
        verify(exactly = 1) { subscriberB.notifyActions(phase, actions) }
        verify(exactly = 1) { subscriberC.notifyActions(phase, actions) }
    }

    @Test
    @DisplayName("""notifyAction(): executing broadcast notifyAction -> all subscribers are notified""")
    fun notifyAction_allSubscribersAreNotified() {
        val gameContext = aGameContext()
        val playerAction: PlayerAction = aPlayer() does call()
        val broadcaster = GameBroadcaster()
        broadcaster.subscribe(subscriberA)
        broadcaster.subscribe(subscriberB)
        broadcaster.subscribe(subscriberC)

        broadcaster.notifyAction(gameContext, playerAction)

        verify(exactly = 1) { subscriberA.notifyAction(gameContext, playerAction) }
        verify(exactly = 1) { subscriberB.notifyAction(gameContext, playerAction) }
        verify(exactly = 1) { subscriberC.notifyAction(gameContext, playerAction) }
    }

}
