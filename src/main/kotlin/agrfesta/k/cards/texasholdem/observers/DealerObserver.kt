package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.rules.gameplay.GamePhase
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerAction
import agrfesta.k.cards.texasholdem.rules.gameplay.ViewHeroGameContext

interface DealerObserver {
    fun notifyActions(phase: GamePhase, actions: List<PlayerAction>) {}
    fun notifyAction(context: ViewHeroGameContext, playerAction: PlayerAction) {}
}

fun multipleDealerObserverOf(vararg observers: DealerObserver?) =
    multipleDealerObserverOf(observers
        .filterNotNull()
        .toSet())

fun multipleDealerObserverOf(observers: Set<DealerObserver>) = object: DealerObserver {
    override fun notifyActions(phase: GamePhase, actions: List<PlayerAction>) {
        observers.forEach { it.notifyActions(phase, actions) }
    }
    override fun notifyAction(context: ViewHeroGameContext, playerAction: PlayerAction) {
        observers.forEach { it.notifyAction(context, playerAction) }
    }
}
