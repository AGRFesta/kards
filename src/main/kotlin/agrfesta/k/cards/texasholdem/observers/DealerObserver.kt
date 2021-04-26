package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.playercontext.PlayerAction
import agrfesta.k.cards.texasholdem.playercontext.PlayerGameContext
import agrfesta.k.cards.texasholdem.rules.gameplay.Board
import agrfesta.k.cards.texasholdem.rules.gameplay.GamePhase
import agrfesta.k.cards.texasholdem.rules.gameplay.SeatName

interface DealerObserver {
    fun notifyActions(phase: GamePhase, actions: List<PlayerAction>) {}
    fun notifyAction(context: PlayerGameContext<SeatName, Board>, playerAction: PlayerAction) {}
}

class MultipleDealerObserver(private val observers: Set<DealerObserver>): DealerObserver {

    override fun notifyActions(phase: GamePhase, actions: List<PlayerAction>) {
        observers.forEach { it.notifyActions(phase, actions) }
    }

    override fun notifyAction(context: PlayerGameContext<SeatName, Board>, playerAction: PlayerAction) {
        observers.forEach { it.notifyAction(context, playerAction) }
    }

}

fun multipleDealerObserverOf(vararg observers: DealerObserver?) =
    MultipleDealerObserver(observers
        .filterNotNull()
        .toSet())
