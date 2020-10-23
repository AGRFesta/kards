package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.playercontext.PlayerAction

class GameContext(val table: Table<InGamePlayer>,
                  val payments: GamePayments,
                  val board: Board,
                  val history: Map<GamePhase,List<PlayerAction>>) {

    fun add(actions: List<PlayerAction>): GameContext {
        val newHistory = history.toMutableMap()
        newHistory[board.info().phase] = actions.toList()
        return GameContext(table, payments, board, newHistory)
    }

    fun nextPhase(): GameContext {
        return GameContext(table, payments, board.next(), history)
    }

}
