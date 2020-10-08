package agrfesta.k.cards.texasholdem.playercontext

import agrfesta.k.cards.texasholdem.rules.gameplay.Opponent
import agrfesta.k.cards.texasholdem.rules.gameplay.Table

class PublicTableData(val players: List<Opponent>, val button: Int)

fun Table.publicData() = PublicTableData(this.players.map { it.asOpponent() }, this.button)
