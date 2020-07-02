package agrfesta.k.cards.texasholdem.playercontext

import agrfesta.k.cards.texasholdem.rules.gameplay.Board
import agrfesta.k.cards.texasholdem.rules.gameplay.GamePayments
import agrfesta.k.cards.texasholdem.rules.gameplay.GamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.OwnPlayer

class PlayerGameContext(
        val me: OwnPlayer,
        val payments: GamePayments,
        val board: Board,
        val potAmount: Int,
        val table: PublicTableData,
        val history: List<PlayerAction>
)