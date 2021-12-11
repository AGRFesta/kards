package org.agrfesta.k.kards.texasholdem.observers

import org.agrfesta.k.kards.texasholdem.rules.gameplay.GameContext
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Player
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStack

interface GameObserver: ShowdownObserver, DealerObserver {
    fun notifyWinner(result: GameResult) {}
    fun notifyStartingPhase(context: GameContext) {}
}

class GameResult(val winner: Player, val prize: Int, val players: List<PlayerStack>)

fun multipleGameObserverOf(vararg observers: GameObserver?) =
    multipleGameObserverOf(observers
        .filterNotNull()
        .toSet())

fun multipleGameObserverOf(observers: Set<GameObserver>): GameObserver = object: GameObserver,
    DealerObserver by multipleDealerObserverOf(observers),
    ShowdownObserver by multipleShowdownObserverOf(observers) {

    override fun notifyWinner(result: GameResult) {
        observers.forEach { it.notifyWinner(result) }
    }

    override fun notifyStartingPhase(context: GameContext) {
        observers.forEach { it.notifyStartingPhase(context) }
    }

}
