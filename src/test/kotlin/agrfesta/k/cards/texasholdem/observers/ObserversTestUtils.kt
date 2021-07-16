package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.rules.gameplay.GamePhase
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerAction
import agrfesta.k.cards.texasholdem.rules.gameplay.GameContext
import agrfesta.k.cards.texasholdem.rules.gameplay.GameContextImpl
import io.mockk.mockk
import io.mockk.verify

// DealerObserver
val GAME_CONTEXT: GameContext = mockk(relaxed = true)
val playerAction: PlayerAction = mockk(relaxed = true)
val gamePhase: GamePhase = mockk(relaxed = true)
val playerActionList: List<PlayerAction> = mockk(relaxed = true)

fun assertNotifiedDealerObserver(observer: DealerObserver) {
    verify(exactly = 1) { observer.notifyAction(GAME_CONTEXT, playerAction) }
    verify(exactly = 1) { observer.notifyActions(gamePhase, playerActionList) }
}
fun assertNotNotifiedDealerObserver(observer: DealerObserver) {
    verify(exactly = 0) { observer.notifyAction(any(), any()) }
    verify(exactly = 0) { observer.notifyActions(any(), any()) }
}

// ShowdownObserver
val showdownPlayerResultCollection: Collection<ShowdownPlayerResult> = mockk(relaxed = true)

fun assertNotifiedShowdownObserver(observer: ShowdownObserver) {
    verify(exactly = 1) { observer.notifyResult(showdownPlayerResultCollection) }
}
fun assertNotNotifiedShowdownObserver(observer: ShowdownObserver) {
    verify(exactly = 0) { observer.notifyResult(any()) }
}

// GameObserver
val gameResult: GameResult = mockk(relaxed = true)
val readOnlyGameContextImpl: GameContextImpl = mockk(relaxed = true)

fun assertNotifiedGameObserver(observer: GameObserver) {
    verify(exactly = 1) { observer.notifyWinner(gameResult) }
    verify(exactly = 1) { observer.notifyStartingPhase(readOnlyGameContextImpl) }
}
fun assertNotNotifiedGameObserver(observer: GameObserver) {
    verify(exactly = 0) { observer.notifyWinner(any()) }
    verify(exactly = 0) { observer.notifyStartingPhase(any()) }
}
