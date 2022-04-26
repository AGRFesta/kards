package org.agrfesta.k.kards.texasholdem.observers

import io.mockk.mockk
import io.mockk.verify
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Player
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerAction
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PublicSittingPlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.aGameContext
import org.agrfesta.k.kards.texasholdem.rules.gameplay.aGameResult
import org.agrfesta.k.kards.texasholdem.rules.gameplay.aPlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.aShowdownPlayerResult
import org.agrfesta.k.kards.texasholdem.rules.gameplay.call
import org.agrfesta.k.kards.texasholdem.rules.gameplay.does
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TournamentBroadcasterTest {
    private val subscriberA = mockk<TournamentObserver>(relaxed = true)
    private val subscriberB = mockk<TournamentObserver>(relaxed = true)
    private val subscriberC = mockk<TournamentObserver>(relaxed = true)

    @Test
    @DisplayName("""notifyWinner(): executing broadcast notifyWinner -> all subscribers are notified""")
    fun notifyWinner_allSubscribersAreNotified() {
        val gameResult = aGameResult()
        val broadcaster = TournamentBroadcaster()
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
        val broadcaster = TournamentBroadcaster()
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
        val broadcaster = TournamentBroadcaster()
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
        val broadcaster = TournamentBroadcaster()
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
        val broadcaster = TournamentBroadcaster()
        broadcaster.subscribe(subscriberA)
        broadcaster.subscribe(subscriberB)
        broadcaster.subscribe(subscriberC)

        broadcaster.notifyAction(gameContext, playerAction)

        verify(exactly = 1) { subscriberA.notifyAction(gameContext, playerAction) }
        verify(exactly = 1) { subscriberB.notifyAction(gameContext, playerAction) }
        verify(exactly = 1) { subscriberC.notifyAction(gameContext, playerAction) }
    }

    @Test
    @DisplayName("""notifyTournamentRanking(): executing broadcast notifyAction -> all subscribers are notified""")
    fun notifyTournamentRanking_allSubscribersAreNotified() {
        val ranking = listOf<PublicSittingPlayer>()
        val losers = listOf<Set<Player>>()
        val broadcaster = TournamentBroadcaster()
        broadcaster.subscribe(subscriberA)
        broadcaster.subscribe(subscriberB)
        broadcaster.subscribe(subscriberC)

        broadcaster.notifyTournamentRanking(ranking, losers)

        verify(exactly = 1) { subscriberA.notifyTournamentRanking(ranking, losers) }
        verify(exactly = 1) { subscriberB.notifyTournamentRanking(ranking, losers) }
        verify(exactly = 1) { subscriberC.notifyTournamentRanking(ranking, losers) }
    }

}
