package agrfesta.k.cards.texasholdem.playercontext

import agrfesta.k.cards.texasholdem.rules.gameplay.Board
import agrfesta.k.cards.texasholdem.rules.gameplay.GamePayments
import agrfesta.k.cards.texasholdem.rules.gameplay.GamePlayer

class PlayerGameContext(
        val hero: GamePlayer,
        val payments: GamePayments,
        val board: Board,
        val potAmount: Int,
        val table: PublicTableData,
        val history: List<PlayerAction>
)