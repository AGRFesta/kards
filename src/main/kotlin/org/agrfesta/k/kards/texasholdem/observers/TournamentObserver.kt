package org.agrfesta.k.kards.texasholdem.observers

import org.agrfesta.k.kards.texasholdem.rules.gameplay.Player
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStack

interface TournamentObserver: GameObserver {
    fun notifyTournamentRanking(ranking: List<PlayerStack>, losers: List<Set<Player>>) {}
}
