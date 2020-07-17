package agrfesta.k.cards.texasholdem.playercontext

import agrfesta.k.cards.texasholdem.rules.gameplay.*

class PlayerGameContext(
        val me: OwnPlayer,
        val payments: GamePayments,
        val board: BoardInfo,
        val potAmount: Int,
        val table: PublicTableData,
        val history: Map<GamePhase,List<PlayerAction>>
)

class PlayerAction(val player: Player, val action: Action)

