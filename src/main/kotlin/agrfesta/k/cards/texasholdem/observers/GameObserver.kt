package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.rules.gameplay.Board
import agrfesta.k.cards.texasholdem.rules.gameplay.Player

interface GameObserver: ShowdownObserver {
    fun notifyWinner(player: Player, prize: Int)
    fun notifyStartingPhase(board: Board)
}
