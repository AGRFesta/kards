package org.agrfesta.k.kards.texasholdem.observers

import io.mockk.mockk
import io.mockk.verify
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerAction
import org.agrfesta.k.kards.texasholdem.rules.gameplay.aGameContext
import org.agrfesta.k.kards.texasholdem.rules.gameplay.aPlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.call
import org.agrfesta.k.kards.texasholdem.rules.gameplay.does
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DealerBroadcasterTest {
    private val subscriberA = mockk<GameObserver>(relaxed = true)
    private val subscriberB = mockk<GameObserver>(relaxed = true)
    private val subscriberC = mockk<GameObserver>(relaxed = true)

    @Test
    @DisplayName("""notifyActions(): executing broadcast notifyActions -> all subscribers are notified""")
    fun notifyActions_allSubscribersAreNotified() {
        val phase = GamePhase.PRE_FLOP
        val actions = emptyList<PlayerAction>()
        val broadcaster = DealerBroadcaster()
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
        val broadcaster = DealerBroadcaster()
        broadcaster.subscribe(subscriberA)
        broadcaster.subscribe(subscriberB)
        broadcaster.subscribe(subscriberC)

        broadcaster.notifyAction(gameContext, playerAction)

        verify(exactly = 1) { subscriberA.notifyAction(gameContext, playerAction) }
        verify(exactly = 1) { subscriberB.notifyAction(gameContext, playerAction) }
        verify(exactly = 1) { subscriberC.notifyAction(gameContext, playerAction) }
    }

}
