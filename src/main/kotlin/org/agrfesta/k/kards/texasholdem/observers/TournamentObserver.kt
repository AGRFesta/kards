package org.agrfesta.k.kards.texasholdem.observers

import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerIdentity
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PublicSittingPlayer

interface TournamentObserver: GameObserver {
    fun notifyTournamentRanking(ranking: List<PublicSittingPlayer>, losers: List<Set<PlayerIdentity>>) {}
}
