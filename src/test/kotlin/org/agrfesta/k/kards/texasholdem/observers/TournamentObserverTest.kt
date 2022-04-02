package org.agrfesta.k.kards.texasholdem.observers

import io.mockk.mockk
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Player
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PublicSittingPlayer
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TournamentObserverTest {
    private val ranking: List<PublicSittingPlayer> = mockk(relaxed = true)
    private val losers: List<Set<Player>> = mockk(relaxed = true)

    @Test
    @DisplayName("""implemented observer without method override -> 
        can be notified but will use default implementation (do nothing)""")
    fun implementationTest000() {
        val observer = object: TournamentObserver {}

        observer.notifyAction(GAME_CONTEXT, playerAction)
        observer.notifyActions(gamePhase, playerActionList)
        observer.notifyWinner(gameResult)
        observer.notifyStartingPhase(readOnlyGameContextImpl)
        observer.notifyResult(showdownPlayerResultCollection)
        observer.notifyTournamentRanking(ranking, losers)
    }
}
