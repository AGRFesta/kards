package org.agrfesta.k.kards.texasholdem.observers

import org.agrfesta.k.kards.texasholdem.rules.gameplay.GameContext
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerAction

interface DealerObserver {
    fun notifyActions(phase: GamePhase, actions: List<PlayerAction>) {}
    fun notifyAction(context: GameContext, playerAction: PlayerAction) {}
}
