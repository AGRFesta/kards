package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.rules.gameplay.Board
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStack

interface GameObserver: ShowdownObserver {
    fun notifyWinner(result: GameResult) {}
    fun notifyStartingPhase(board: Board) {}
}

class GameResult(val winner: Player, val prize: Int, val players: List<PlayerStack>)
