package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.rules.gameplay.GamePhase
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerAction
import agrfesta.k.cards.texasholdem.rules.gameplay.ViewHeroGameContext

interface DealerObserver {
    fun notifyActions(phase: GamePhase, actions: List<PlayerAction>) {}
    fun notifyAction(context: ViewHeroGameContext, playerAction: PlayerAction) {}
}

class MultipleDealerObserver(private val observers: Set<DealerObserver>): DealerObserver {

    override fun notifyActions(phase: GamePhase, actions: List<PlayerAction>) {
        observers.forEach { it.notifyActions(phase, actions) }
    }

    override fun notifyAction(context: ViewHeroGameContext, playerAction: PlayerAction) {
        observers.forEach { it.notifyAction(context, playerAction) }
    }

}

fun multipleDealerObserverOf(vararg observers: DealerObserver?) =
    MultipleDealerObserver(observers
        .filterNotNull()
        .toSet())
