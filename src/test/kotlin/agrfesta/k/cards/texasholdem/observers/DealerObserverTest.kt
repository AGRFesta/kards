package agrfesta.k.cards.texasholdem.observers

import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DealerObserverTest {

    @Test
    @DisplayName("""multipleDealerObserverOf(): multi observer composed by null and non-null observer -> 
        only one observer will be notified""")
    fun multipleDealerObserverOfTest000() {
        val observer: DealerObserver = mockk(relaxed = true)
        val multipleDealerObserver = multipleDealerObserverOf(null, observer)

        multipleDealerObserver.notifyAction(viewHeroGameContext, playerAction)
        multipleDealerObserver.notifyActions(gamePhase, playerActionList)

        assertNotifiedDealerObserver(observer)
    }
    @Test
    @DisplayName("""multipleDealerObserverOf(): multi observer composed by two observer -> 
        both observer will be notified""")
    fun multipleDealerObserverOfTest001() {
        val observerA: DealerObserver = mockk(relaxed = true)
        val observerB: DealerObserver = mockk(relaxed = true)
        val observerC: DealerObserver = mockk(relaxed = true)
        val multipleDealerObserver = multipleDealerObserverOf(observerA, observerB)

        multipleDealerObserver.notifyAction(viewHeroGameContext, playerAction)
        multipleDealerObserver.notifyActions(gamePhase, playerActionList)

        assertNotifiedDealerObserver(observerA)
        assertNotifiedDealerObserver(observerB)
        assertNotNotifiedDealerObserver(observerC)
    }

    @Test
    @DisplayName("""implemented observer without method override -> 
        can be notified but will use default implementation (do nothing)""")
    fun implementationTest000() {
        val observer = object: DealerObserver {}

        observer.notifyAction(viewHeroGameContext, playerAction)
        observer.notifyActions(gamePhase, playerActionList)
    }

}
