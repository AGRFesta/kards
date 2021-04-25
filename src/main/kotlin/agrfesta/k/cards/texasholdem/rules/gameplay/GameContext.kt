package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.playercontext.PlayerAction
import java.util.*

class GameContext<T: SeatName>(
    val uuid: UUID,
    val table: Table<T>,
    val payments: GamePayments,
    val board: Board,
    val history: Map<GamePhase,List<PlayerAction>>) {

    fun add(actions: List<PlayerAction>): GameContext<T> {
        val newHistory = history.toMutableMap()
        newHistory[board.info().phase] = actions.toList()
        return GameContext(uuid, table, payments, board, newHistory)
    }

    fun nextPhase(): GameContext<T> {
        return GameContext(uuid, table, payments, board.next(), history)
    }

}
