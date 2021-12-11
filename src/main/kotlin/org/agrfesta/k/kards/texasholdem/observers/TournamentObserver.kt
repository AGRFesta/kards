package org.agrfesta.k.kards.texasholdem.observers

import org.agrfesta.k.kards.texasholdem.rules.gameplay.Player
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStack

interface TournamentObserver: GameObserver {
    fun notifyTournamentRanking(ranking: List<PlayerStack>, losers: List<Set<Player>>) {}
}

fun multipleTournamentObserverOf(vararg observers: TournamentObserver?) =
    multipleTournamentObserverOf(observers
        .filterNotNull()
        .toSet())

fun multipleTournamentObserverOf(observers: Set<TournamentObserver>): TournamentObserver = object: TournamentObserver,
    GameObserver by multipleGameObserverOf(observers) {

    override fun notifyTournamentRanking(ranking: List<PlayerStack>, losers: List<Set<Player>>) {
        observers.forEach { it.notifyTournamentRanking(ranking, losers) }
    }

}

