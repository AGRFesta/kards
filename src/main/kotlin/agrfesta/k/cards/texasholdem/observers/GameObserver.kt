package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.rules.gameplay.Board
import agrfesta.k.cards.texasholdem.rules.gameplay.Player

interface GameObserver: ShowdownObserver {

    //TODO test
    fun notifyWinner(player: Player, prize: Int)

    //TODO test
    fun notifyStartingPhase(board: Board)
}