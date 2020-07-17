package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.playercontext.PlayerAction

//TODO should implement a check for a coherence between board and history, example: a bord preflop can't have a history
// at Turn
class GameContext(val table: Table,
                  val payments: GamePayments,
                  val board: Board,
                  val history: Map<GamePhase,List<PlayerAction>>) {

    //TODO test
    fun add(actions: List<PlayerAction>): GameContext {
        val newHistory = history.toMutableMap()
        newHistory[board.info().phase] = actions.toList()
        return GameContext(table, payments, board, newHistory)
    }

    //TODO test
    fun nextPhase(): GameContext {
        return GameContext(table, payments, board.next(), history)
    }

}
