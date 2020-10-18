package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.rules.gameplay.Board
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStack

interface GameObserver: ShowdownObserver {
    fun notifyWinner(result: GameResult)
    fun notifyStartingPhase(board: Board)
}

class MultipleGameObserverAdapter(private val observers: Set<GameObserver?>): GameObserver {
    override fun notifyWinner(result: GameResult) {
        observers.forEach { it?.notifyWinner(result) }
    }

    override fun notifyStartingPhase(board: Board) {
        observers.forEach { it?.notifyStartingPhase(board) }
    }

    override fun notifyResult(result: Collection<ShowdownPlayerResult>) {
        observers.forEach { it?.notifyResult(result) }
    }
}
fun multipleGameObserversOf(vararg observers: GameObserver?): GameObserver =
        MultipleGameObserverAdapter(observers.toSet())

class GameResult(val winner: Player, val prize: Int, val players: List<PlayerStack>)
