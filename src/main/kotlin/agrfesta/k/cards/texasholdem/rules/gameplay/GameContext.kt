package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.playercontext.PlayerAction
import java.util.*

class GameContext<T: SeatName, B: Board>(
    val uuid: UUID,
    val table: Table<T>,
    val payments: GamePayments,
    val board: B,
    val history: Map<GamePhase,List<PlayerAction>>) {

    fun add(actions: List<PlayerAction>): GameContext<T, B> {
        val newHistory = history.toMutableMap()
        newHistory[board.phase()] = actions.toList()
        return GameContext(uuid, table, payments, board, newHistory)
    }

}

fun <T: SeatName> GameContext<T, BoardInSequence>.nextPhase(): GameContext<T, BoardInSequence> =
    GameContext(uuid, table, payments, board.next(), history)
