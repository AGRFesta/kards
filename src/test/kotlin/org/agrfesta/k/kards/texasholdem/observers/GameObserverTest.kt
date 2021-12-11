package org.agrfesta.k.kards.texasholdem.observers

import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Game Observer tests")
class GameObserverTest {

    @Test
    @DisplayName("""multipleGameObserverOf(): multi observer composed by null and non-null observer -> 
        only one observer will be notified""")
    fun multipleGameObserverOfTest000() {
        val observer: GameObserver = mockk(relaxed = true)
        val multipleGameObserver = multipleGameObserverOf(null, observer)

        multipleGameObserver.notifyWinner(gameResult)
        multipleGameObserver.notifyStartingPhase(readOnlyGameContextImpl)

        assertNotifiedGameObserver(observer)
    }
    @Test
    @DisplayName("""multipleGameObserverOf(): multi observer composed by two observer -> 
        both observer will be notified""")
    fun multipleGameObserverOfTest001() {
        val observerA: GameObserver = mockk(relaxed = true)
        val observerB: GameObserver = mockk(relaxed = true)
        val observerC: GameObserver = mockk(relaxed = true)
        val multipleGameObserver = multipleGameObserverOf(observerA, observerB)

        multipleGameObserver.notifyWinner(gameResult)
        multipleGameObserver.notifyStartingPhase(readOnlyGameContextImpl)

        assertNotifiedGameObserver(observerA)
        assertNotifiedGameObserver(observerB)
        assertNotNotifiedGameObserver(observerC)
    }
    @Test
    @DisplayName("""multipleGameObserverOf(): multi observer composed by two observer -> 
        both observer will be notified as showdown observers""")
    fun multipleGameObserverOfTest002() {
        val observerA: GameObserver = mockk(relaxed = true)
        val observerB: GameObserver = mockk(relaxed = true)
        val observerC: GameObserver = mockk(relaxed = true)
        val multipleGameObserver = multipleGameObserverOf(observerA, observerB)

        multipleGameObserver.notifyResult(showdownPlayerResultCollection)

        assertNotifiedShowdownObserver(observerA)
        assertNotifiedShowdownObserver(observerB)
        assertNotNotifiedShowdownObserver(observerC)
    }
    @Test
    @DisplayName("""multipleGameObserverOf(): multi observer composed by two observer -> 
        both observer will be notified as dealer observers""")
    fun multipleGameObserverOfTest003() {
        val observerA: GameObserver = mockk(relaxed = true)
        val observerB: GameObserver = mockk(relaxed = true)
        val observerC: GameObserver = mockk(relaxed = true)
        val multipleGameObserver = multipleGameObserverOf(observerA, observerB)

        multipleGameObserver.notifyAction(GAME_CONTEXT, playerAction)
        multipleGameObserver.notifyActions(gamePhase, playerActionList)

        assertNotifiedDealerObserver(observerA)
        assertNotifiedDealerObserver(observerB)
        assertNotNotifiedDealerObserver(observerC)
    }

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
