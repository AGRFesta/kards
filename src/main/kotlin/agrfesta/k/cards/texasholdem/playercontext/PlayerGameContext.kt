package agrfesta.k.cards.texasholdem.playercontext

import agrfesta.k.cards.texasholdem.rules.gameplay.*

class PlayerGameContext(
        val me: OwnPlayer,
        val payments: GamePayments,
        val board: BoardInfo,
        val potAmount: Int,
        val table: PublicTableData,
        val history: Map<GamePhase,List<PlayerAction>>) {
    fun getActions(phase: GamePhase) = history.getOrElse(phase){ emptyList() }
    fun actualActions() = getActions(board.phase)
}

class PlayerAction(val player: Player, val action: Action) {
    override fun toString() = "${player.name} $action"
}

