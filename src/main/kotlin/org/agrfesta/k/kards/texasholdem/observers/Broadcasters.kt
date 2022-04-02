package org.agrfesta.k.kards.texasholdem.observers

import org.agrfesta.k.kards.texasholdem.rules.gameplay.GameContext
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerAction
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerIdentity
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PublicSittingPlayer

/**
 * Object used to notify an information to all its subscribers.
 * @param T type of the subscribers
 */
interface Broadcaster<T> {
    /**
     * Subscribe the specified [subscriber].
     */
    fun subscribe(subscriber: T)

    /**
     * Runs the specified [notifier] for all subscribers.
     */
    fun notify(notifier: (T) -> Unit)
}

class BroadcasterImpl<T>: Broadcaster<T> {
    private val subscribers: MutableSet<T> = mutableSetOf()

    override fun subscribe(subscriber: T) {
        subscribers.add(subscriber)
    }

    override fun notify(notifier: (T) -> Unit) {
        subscribers.forEach { notifier(it) }
    }
}

class ShowdownBroadcaster: ShowdownObserver, Broadcaster<ShowdownObserver> by BroadcasterImpl() {
    override fun notifyResult(result: Collection<ShowdownPlayerResult>) {
        notify { it.notifyResult(result) }
    }
}

class DealerBroadcaster: DealerObserver, Broadcaster<DealerObserver> by BroadcasterImpl() {
    override fun notifyActions(phase: GamePhase, actions: List<PlayerAction>) {
        notify { it.notifyActions(phase, actions) }
    }

    override fun notifyAction(context: GameContext, playerAction: PlayerAction) {
        notify { it.notifyAction(context, playerAction) }
    }
}
fun dealerBroadcasterFor(vararg observers: DealerObserver?): DealerBroadcaster {
    val broadcaster = DealerBroadcaster()
    observers
        .filterNotNull()
        .forEach { broadcaster.subscribe(it) }
    return broadcaster
}

class GameBroadcaster(
    private val gameBroadcaster: BroadcasterImpl<GameObserver> = BroadcasterImpl(),
    private val showdownBroadcaster: ShowdownBroadcaster = ShowdownBroadcaster(),
    private val dealerBroadcaster: DealerBroadcaster = DealerBroadcaster()
): GameObserver, Broadcaster<GameObserver> by gameBroadcaster,
    ShowdownObserver by showdownBroadcaster,
    DealerObserver by dealerBroadcaster {

    override fun subscribe(subscriber: GameObserver) {
        gameBroadcaster.subscribe(subscriber)
        showdownBroadcaster.subscribe(subscriber)
        dealerBroadcaster.subscribe(subscriber)
    }

    override fun notifyWinner(result: GameResult) {
        notify { it.notifyWinner(result) }
    }

    override fun notifyStartingPhase(context: GameContext) {
        notify { it.notifyStartingPhase(context) }
    }
}

class TournamentBroadcaster(
    private val tournamentBroadcaster: BroadcasterImpl<TournamentObserver> = BroadcasterImpl(),
    private val gameBroadcaster: GameBroadcaster = GameBroadcaster()
): TournamentObserver, Broadcaster<TournamentObserver> by tournamentBroadcaster, GameObserver by gameBroadcaster {

    override fun subscribe(subscriber: TournamentObserver) {
        tournamentBroadcaster.subscribe(subscriber)
        gameBroadcaster.subscribe(subscriber)
    }

    override fun notifyTournamentRanking(ranking: List<PublicSittingPlayer>, losers: List<Set<PlayerIdentity>>) {
        notify { it.notifyTournamentRanking(ranking, losers) }
    }

}
