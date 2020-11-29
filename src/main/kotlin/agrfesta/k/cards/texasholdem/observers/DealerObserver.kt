package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.playercontext.PlayerAction
import agrfesta.k.cards.texasholdem.playercontext.PlayerGameContext
import agrfesta.k.cards.texasholdem.rules.gameplay.GamePhase

interface DealerObserver {
    fun notifyActions(phase: GamePhase, actions: List<PlayerAction>)
    fun notifyAction(context: PlayerGameContext, playerAction: PlayerAction) {}
}
