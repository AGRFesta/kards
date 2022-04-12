package org.agrfesta.k.kards.texasholdem.observers

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Game Observer tests")
class GameObserverTest {

    @Test
    @DisplayName("""implemented observer without method override -> 
        can be notified but will use default implementation (do nothing)""")
    fun implementationTest000() {
        val observer = object: GameObserver {}

        observer.notifyAction(GAME_CONTEXT, playerAction)
        observer.notifyActions(gamePhase, playerActionList)
        observer.notifyWinner(gameResult)
        observer.notifyStartingPhase(readOnlyGameContextImpl)
        observer.notifyResult(showdownPlayerResultCollection)
    }

}
