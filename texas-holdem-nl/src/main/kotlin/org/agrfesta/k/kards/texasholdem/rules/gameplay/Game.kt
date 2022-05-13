package org.agrfesta.k.kards.texasholdem.rules.gameplay

import org.agrfesta.k.cards.playingcards.deck.Deck
import org.agrfesta.k.cards.playingcards.suits.Suit.FRENCH
import org.agrfesta.k.kards.texasholdem.observers.DealerObserver
import org.agrfesta.k.kards.texasholdem.observers.GameObserver
import org.agrfesta.k.kards.texasholdem.observers.GameResult
import org.agrfesta.k.kards.texasholdem.observers.ShowdownObserver
import org.agrfesta.k.kards.texasholdem.observers.dealerBroadcasterFor
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluatorBaseImpl
import org.agrfesta.k.kards.texasholdem.utils.DistinctList
import org.agrfesta.k.kards.texasholdem.utils.UuidProvider
import java.util.*

interface Game {
    val uuid: UUID
    fun play(): DistinctList<SittingPlayer>
}

/**
 * Game Configuration
 *
 * Groups all possible customizable aspect of the [Game].
 *
 * @property uuidProvider defines the UUID provider for the [Game]. When we create a [Game] if the UUID is not assigned
 * the constructor will generate one using this provider.
 *
 * @property dealerFactory chosen implementation for the [DealerFactory].
 *
 * @property createShowdown defines the [Showdown] provider for the [Game]. It will instantiate a [Showdown]
 * implementation injecting all [Game] observers.
 */
class GameConfig(
    val uuidProvider: UuidProvider = { UUID.randomUUID() },
    val dealerFactory: DealerFactory = DealerFactoryImpl(),
    val createShowdown: (ShowdownObserver?) -> Showdown = { ShowdownImpl(CardsEvaluatorBaseImpl(),it) }
)

class GameImpl(
    uuid: UUID? = null,
    payments: GamePayments,
    table: Table<SittingPlayer>,
    deck: Deck = FRENCH.createDeck(),
    private val observer: GameObserver? = null,
    private val config: GameConfig = GameConfig()
) : Game, DealerObserver {
    override val uuid: UUID = uuid ?: config.uuidProvider()
    private val showdown: Showdown = config.createShowdown(observer)
    private var context: MutableGameContextImpl

    init {
        val inGameTable: Table<InGamePlayer> = table
            .map { InGamePlayerImpl(sittingPlayer = it, cards = deck.draw(2).toSet()) }
        val phasePots = emptyPhasePots { mutableMapOf() }
        context = MutableGameContextImpl(
            uuid = this.uuid,
            table = inGameTable,
            payments = payments,
            board = EmptyBoard(deck) as BoardInSequence,
            phasePots = phasePots)
    }

    override fun play(): DistinctList<SittingPlayer> {
        // Pre-Flop
        findPreFlopWinner() ?:

        // Flop
        findWinner() ?:

        // Turn
        findWinner() ?:

        // River
        findWinner() ?:

        // Showdown
        showdown.execute(context.getGlobalPot(), context.board, context.table)

        return context.table.players.map { it.asSittingPlayer() }
    }

    private fun findWinner(players: List<InGamePlayer>): InGamePlayer? {
        val winner = players.findWinner()
        if (winner != null) {
            winner.receive(context.getGlobalPot().amount())
            observer?.notifyWinner(
                GameResult(winner, context.getGlobalPot().amount(), players.map { it.asPublicInGamePlayer() } )
            )
        }
        return winner
    }

    private fun findPreFlopWinner(): InGamePlayer? {
        observer?.notifyStartingPhase(context.toGameContext())
        config.dealerFactory.preFlopDealer(context, dealerBroadcasterFor(this, observer))
            .collectPot()
        return findWinner(context.table.players)
    }

    private fun findWinner(): InGamePlayer? {
        context.board = context.board.next()
        observer?.notifyStartingPhase(context.toGameContext())
        config.dealerFactory.postFlopDealer(context, dealerBroadcasterFor(this, observer))
            .collectPot()
        return findWinner(context.table.players)
    }

    override fun notifyActions(phase: GamePhase, actions: List<PlayerAction>) {
        val newHistory = context.history.toMutableMap()
        newHistory[context.board.phase] = actions.toMutableList()
        context = MutableGameContextImpl(
            context.uuid, context.table, context.payments, context.board, newHistory, context.phasePots)
    }
}
