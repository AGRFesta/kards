package org.agrfesta.k.kards.texasholdem.observers

import org.agrfesta.k.kards.texasholdem.rules.gameplay.GameContext
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerIdentity
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PublicInGamePlayer

interface GameObserver: ShowdownObserver, DealerObserver {
    fun notifyWinner(result: GameResult) {}
    fun notifyStartingPhase(context: GameContext) {}
}

class GameResult(val winner: PlayerIdentity, val prize: UInt, val players: List<PublicInGamePlayer>)

