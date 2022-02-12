package org.agrfesta.k.kards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.deck.Deck
import agrfesta.k.cards.playingcards.suits.Suit.FRENCH
import org.agrfesta.k.kards.texasholdem.observers.DealerObserver
import org.agrfesta.k.kards.texasholdem.observers.GameObserver
import org.agrfesta.k.kards.texasholdem.observers.GameResult
import org.agrfesta.k.kards.texasholdem.observers.ShowdownObserver
import org.agrfesta.k.kards.texasholdem.observers.multipleDealerObserverOf
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluatorBaseImpl
import org.agrfesta.k.kards.texasholdem.utils.UuidProvider
import java.util.*

interface Game {
    fun getId(): UUID
    fun play(): List<PlayerStack>
}

class GameImpl(
    payments: GamePayments,
    table: Table<PlayerStack>,
    private val dealerFactory: DealerFactory = DealerFactoryImpl(),
    private val observer: GameObserver? = null,
    deck: Deck = FRENCH.createDeck(),
    uuidProvider: UuidProvider = { UUID.randomUUID() },
    showdownProvider: (ShowdownObserver?) -> Showdown = { ShowdownImpl(CardsEvaluatorBaseImpl(),it) }
) : Game, DealerObserver {
    private val showdown: Showdown = showdownProvider(observer)
    private var context: MutableGameContextImpl

    init {
        val inGameTable = table.map { InGamePlayer(it.player, it.stack, deck.draw(2).toSet()) }
        val phasePots = emptyPhasePots<InGamePlayer, MutableMap<InGamePlayer, Int>> { mutableMapOf() }
        context = MutableGameContextImpl(
            uuid = uuidProvider.invoke(),
            table = inGameTable,
            payments = payments,
            board = EmptyBoard(deck) as BoardInSequence,
            phasePots = phasePots)
    }

    override fun getId() = context.uuid

    override fun play(): List<PlayerStack> {
        // Pre-Flop
        findPreFlopWinner() ?:

        // Flop
        findWinner() ?:

        // Turn
        findWinner() ?:

        // River
        findWinner() ?:

        // Showdown
        showdown.execute(context.getGlobalPot(), context.board)

        return context.table.players.toPlayerStack()
    }

    private fun findWinner(players: List<InGamePlayer>): InGamePlayer? {
        val winner = players.findWinner()
        if (winner != null) {
            winner.receive(context.getGlobalPot().amount())
            observer?.notifyWinner(
                GameResult(winner.player, context.getGlobalPot().amount(), players.toPlayerStack() )
            )
        }
        return winner
    }

    private fun findPreFlopWinner(): InGamePlayer? {
        observer?.notifyStartingPhase(context.toGameContext())
        dealerFactory.preFlopDealer(context, multipleDealerObserverOf(this, observer))
            .collectPot()
        return findWinner(context.table.players)
    }

    private fun findWinner(): InGamePlayer? {
        context.board = context.board.next()
        observer?.notifyStartingPhase(context.toGameContext())
        dealerFactory.postFlopDealer(context, multipleDealerObserverOf(this, observer))
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
