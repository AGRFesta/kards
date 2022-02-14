package org.agrfesta.k.kards.texasholdem.observers

import org.agrfesta.k.kards.texasholdem.rules.gameplay.GameContext
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Player
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStack

interface GameObserver: ShowdownObserver, DealerObserver {
    fun notifyWinner(result: GameResult) {}
    fun notifyStartingPhase(context: GameContext) {}
}

class GameResult(val winner: Player, val prize: Int, val players: List<PlayerStack>)
