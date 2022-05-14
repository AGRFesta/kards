package org.agrfesta.k.kards.texasholdem.observers

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DealerObserverTest {

    @Test
    @DisplayName("""implemented observer without method override -> 
        can be notified but will use default implementation (do nothing)""")
    fun implementationTest000() {
        val observer = object: DealerObserver {}

        observer.notifyAction(GAME_CONTEXT, playerAction)
        observer.notifyActions(gamePhase, playerActionList)
    }

}
