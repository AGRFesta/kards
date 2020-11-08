package agrfesta.k.cards.texasholdem.playercontext

import agrfesta.k.cards.texasholdem.rules.gameplay.Action
import agrfesta.k.cards.texasholdem.rules.gameplay.BoardInfo
import agrfesta.k.cards.texasholdem.rules.gameplay.GamePayments
import agrfesta.k.cards.texasholdem.rules.gameplay.GamePhase
import agrfesta.k.cards.texasholdem.rules.gameplay.Opponent
import agrfesta.k.cards.texasholdem.rules.gameplay.OwnPlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.SeatName
import agrfesta.k.cards.texasholdem.rules.gameplay.Table

class PlayerGameContext(
        val me: OwnPlayer,
        val payments: GamePayments,
        val board: BoardInfo,
        val potAmount: Int,
        val table: Table<Opponent>,
        val history: Map<GamePhase,List<PlayerAction>>) {
    fun getActions(phase: GamePhase) = history.getOrElse(phase){ emptyList() }
    fun getActualActions() = getActions(board.phase)
}

data class PlayerAction(val playerName: String, val action: Action) {
    override fun toString() = "$playerName $action"
}
infix fun SeatName.does(action: Action) = PlayerAction(this.getSeatName(), action)
