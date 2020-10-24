package agrfesta.k.cards.texasholdem.playercontext

import agrfesta.k.cards.texasholdem.rules.gameplay.BoardInfo
import agrfesta.k.cards.texasholdem.rules.gameplay.GamePayments
import agrfesta.k.cards.texasholdem.rules.gameplay.GamePhase
import agrfesta.k.cards.texasholdem.rules.gameplay.Opponent
import agrfesta.k.cards.texasholdem.rules.gameplay.OwnPlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.Table
import io.mockk.mockk

class PlayerGameContextTestBuilder {
    private var me: OwnPlayer = mockk(relaxed = true)
    private var payments: GamePayments = mockk(relaxed = true)
    private var board: BoardInfo = mockk(relaxed = true)
    private var potAmount: Int = mockk(relaxed = true)
    private var table: Table<Opponent> = mockk(relaxed = true)
    private var history: MutableMap<GamePhase, List<PlayerAction>> = mutableMapOf()

    fun withHistory(history: Map<GamePhase, List<PlayerAction>>): PlayerGameContextTestBuilder {
        this.history = history.toMutableMap()
        return this
    }
    fun withPreFlopHistoryWhere(vararg playerActions: PlayerAction): PlayerGameContextTestBuilder {
        this.history[GamePhase.PRE_FLOP] = playerActions.toList()
        return this
    }
    fun withFlopHistoryWhere(vararg playerActions: PlayerAction): PlayerGameContextTestBuilder {
        this.history[GamePhase.FLOP] = playerActions.toList()
        return this
    }
    fun withBoard(board: BoardInfo): PlayerGameContextTestBuilder {
        this.board = board
        return this
    }

    fun build() = PlayerGameContext(me, payments, board, potAmount, table, history)

}

fun buildingAPlayerGameContext() = PlayerGameContextTestBuilder()
