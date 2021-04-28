package agrfesta.k.cards.texasholdem.rules.gameplay

import java.util.*

class GameContext<T: SeatName, B: Board>(
    val uuid: UUID,
    val table: Table<T>,
    val payments: GamePayments,
    val board: B,
    val history: Map<GamePhase, List<PlayerAction>>,
    val phasePots: Map<GamePhase, Pot> = mapOf(
        GamePhase.PRE_FLOP to buildPot(),
        GamePhase.FLOP to buildPot(),
        GamePhase.TURN to buildPot(),
        GamePhase.RIVER to buildPot()
    )) {

    fun add(actions: List<PlayerAction>): GameContext<T, B> {
        val newHistory = history.toMutableMap()
        newHistory[board.phase()] = actions.toList()
        return GameContext(uuid, table, payments, board, newHistory, phasePots)
    }

    fun <N: SeatName, M: Board> map(
        tablePlayerMapper: ((T) -> N)? = null,
        boardMapper: ((B) -> M)? = null )
    : GameContext<N, M> = GameContext(
        uuid,
        table.map { tablePlayerMapper?.invoke(it) ?: it as N },
        payments,
        boardMapper?.invoke(board) ?: board as M,
        history,
        phasePots )

    fun getActions(phase: GamePhase) = history.getOrElse(phase){ emptyList() }
    fun getActualPot() = phasePots[board.phase()]
        ?: throw IllegalStateException("Pot not initialized at ${board.phase()}")
    fun getGlobalPot() = phasePots.values.reduce { a, b -> a + b }

}

fun <T: SeatName> GameContext<T, BoardInSequence>.nextPhase(): GameContext<T, BoardInSequence> =
    GameContext(uuid, table, payments, board.next(), history, phasePots)

data class PlayerAction(val playerName: String, val action: Action) {
    override fun toString() = "$playerName $action"
}
infix fun SeatName.does(action: Action) = PlayerAction(this.getSeatName(), action)
