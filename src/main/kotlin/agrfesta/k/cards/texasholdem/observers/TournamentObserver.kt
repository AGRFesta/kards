package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStack

interface TournamentObserver: GameObserver {
    fun notifyTournamentRanking(ranking: List<PlayerStack>, losers: List<Set<Player>>)
}
