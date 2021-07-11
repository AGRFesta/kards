package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStack
import agrfesta.k.cards.texasholdem.rules.gameplay.ViewGameContext

interface GameObserver: ShowdownObserver, DealerObserver {
    fun notifyWinner(result: GameResult) {}
    fun notifyStartingPhase(context: ViewGameContext) {}
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

    override fun notifyStartingPhase(context: ViewGameContext) {
        observers.forEach { it.notifyStartingPhase(context) }
    }

}
