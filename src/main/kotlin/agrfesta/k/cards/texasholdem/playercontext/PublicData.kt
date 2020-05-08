package agrfesta.k.cards.texasholdem.playercontext

import agrfesta.k.cards.texasholdem.rules.gameplay.GamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus
import agrfesta.k.cards.texasholdem.rules.gameplay.Table

class PublicPlayerData(val player: Player, val stack: Int, val state: PlayerStatus)

class PublicTableData(val players: List<PublicPlayerData>, val button: Int)

//TODO test
fun GamePlayer.publicData() = PublicPlayerData(this.player,this.stack,this.status)
//TODO test
fun Table.publicData() = PublicTableData(this.players.map { it.publicData() }, this.button)