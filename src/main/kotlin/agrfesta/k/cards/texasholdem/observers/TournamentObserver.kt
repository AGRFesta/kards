package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.rules.gameplay.GamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.Player

//TODO tests
interface TournamentObserver: GameObserver {
    fun notifyTournamentRanking(ranking: List<PlayerRank>, losers: List<Set<Player>>)
}

class PlayerRank(val player: Player, val stack: Int)

fun Collection<GamePlayer>.toRanking() = this
        .map { PlayerRank(it.player, it.stack) }
        .sortedBy { it.stack }
