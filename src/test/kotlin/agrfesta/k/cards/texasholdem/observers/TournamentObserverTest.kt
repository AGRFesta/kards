package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStack
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TournamentObserverTest {
    private val ranking: List<PlayerStack> = mockk(relaxed = true)
    private val losers: List<Set<Player>> = mockk(relaxed = true)

    private fun assertNotifiedObserver(observer: TournamentObserver) {
        verify(exactly = 1) { observer.notifyTournamentRanking(ranking, losers) }
    }
    private fun assertNotNotifiedObserver(observer: TournamentObserver) {
        verify(exactly = 0) { observer.notifyTournamentRanking(any(), any()) }
    }

    @Test
    @DisplayName("""multipleTournamentObserverOf(): multi observer composed by null and non-null observer -> 
        only one observer will be notified""")
    fun multipleTournamentObserverOfTest000() {
        val observer: TournamentObserver = mockk(relaxed = true)
        val multipleTournamentObserver = multipleTournamentObserverOf(null, observer)

        multipleTournamentObserver.notifyTournamentRanking(ranking, losers)

        assertNotifiedObserver(observer)
    }
    @Test
    @DisplayName("""multipleTournamentObserverOf(): multi observer composed by two observer -> 
        both observer will be notified""")
    fun multipleTournamentObserverOfTest001() {
        val observerA: TournamentObserver = mockk(relaxed = true)
        val observerB: TournamentObserver = mockk(relaxed = true)
        val observerC: TournamentObserver = mockk(relaxed = true)
        val multipleTournamentObserver = multipleTournamentObserverOf(observerA, observerB)

        multipleTournamentObserver.notifyTournamentRanking(ranking, losers)

        assertNotifiedObserver(observerA)
        assertNotifiedObserver(observerB)
        assertNotNotifiedObserver(observerC)
    }
    @Test
    @DisplayName("""multipleTournamentObserverOf(): multi observer composed by two observer -> 
        both observer will be notified as game observers""")
    fun multipleTournamentObserverOfTest002() {
        val observerA: TournamentObserver = mockk(relaxed = true)
        val observerB: TournamentObserver = mockk(relaxed = true)
        val observerC: TournamentObserver = mockk(relaxed = true)
        val multipleTournamentObserver = multipleTournamentObserverOf(observerA, observerB)

        multipleTournamentObserver.notifyWinner(gameResult)
        multipleTournamentObserver.notifyStartingPhase(viewGameContext)

        assertNotifiedGameObserver(observerA)
        assertNotifiedGameObserver(observerB)
        assertNotNotifiedGameObserver(observerC)
    }
    @Test
    @DisplayName("""multipleTournamentObserverOf(): multi observer composed by two observer -> 
        both observer will be notified as showdown observers""")
    fun multipleTournamentObserverOfTest003() {
        val observerA: TournamentObserver = mockk(relaxed = true)
        val observerB: TournamentObserver = mockk(relaxed = true)
        val observerC: TournamentObserver = mockk(relaxed = true)
        val multipleTournamentObserver = multipleTournamentObserverOf(observerA, observerB)

        multipleTournamentObserver.notifyResult(showdownPlayerResultCollection)

        assertNotifiedShowdownObserver(observerA)
        assertNotifiedShowdownObserver(observerB)
        assertNotNotifiedShowdownObserver(observerC)
    }
    @Test
    @DisplayName("""multipleTournamentObserverOf(): multi observer composed by two observer -> 
        both observer will be notified as dealer observers""")
    fun multipleTournamentObserverOfTest004() {
        val observerA: TournamentObserver = mockk(relaxed = true)
        val observerB: TournamentObserver = mockk(relaxed = true)
        val observerC: TournamentObserver = mockk(relaxed = true)
        val multipleTournamentObserver = multipleTournamentObserverOf(observerA, observerB)

        multipleTournamentObserver.notifyAction(viewHeroGameContext, playerAction)
        multipleTournamentObserver.notifyActions(gamePhase, playerActionList)

        assertNotifiedDealerObserver(observerA)
        assertNotifiedDealerObserver(observerB)
        assertNotNotifiedDealerObserver(observerC)
    }

    @Test
    @DisplayName("""implemented observer without method override -> 
        can be notified but will use default implementation (do nothing)""")
    fun implementationTest000() {
        val observer = object: TournamentObserver {}

        observer.notifyAction(viewHeroGameContext, playerAction)
        observer.notifyActions(gamePhase, playerActionList)
        observer.notifyWinner(gameResult)
        observer.notifyStartingPhase(viewGameContext)
        observer.notifyResult(showdownPlayerResultCollection)
        observer.notifyTournamentRanking(ranking, losers)
    }
}
