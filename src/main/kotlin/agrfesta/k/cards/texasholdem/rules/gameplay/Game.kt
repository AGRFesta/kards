package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.observers.DealerObserver
import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.texasholdem.observers.GameResult
import agrfesta.k.cards.texasholdem.observers.multipleDealerObserverOf
import java.util.*

interface Game {
    fun getId(): UUID
    fun play(): List<PlayerStack>
}

class GameImpl(
        private var context: InGameContext,
        private val dealerFactory: DealerFactory,
        private val showdown: Showdown,
        private val observer: GameObserver?
) : Game, DealerObserver {
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
            observer?.notifyWinner(
                GameResult(winner.player, context.getGlobalPot().amount(), players.toPlayerStack() ))
            winner.receive(context.getGlobalPot().amount())
        }
        return winner
    }

    private fun findPreFlopWinner(): InGamePlayer? {
        observer?.notifyStartingPhase(context.toViewGameContext())
        val dealer = dealerFactory.preFlopDealer(context, multipleDealerObserverOf(this, observer))
        dealer.collectPot()
        return findWinner(context.table.players)
    }

    private fun findWinner(): InGamePlayer? {
        context = context.nextPhase()
        observer?.notifyStartingPhase(context.toViewGameContext())
        val dealer = dealerFactory.postFlopDealer(context, multipleDealerObserverOf(this, observer))
        dealer.collectPot()
        return findWinner(context.table.players)
    }

    override fun notifyActions(phase: GamePhase, actions: List<PlayerAction>) {
        val newHistory = context.history.toMutableMap()
        newHistory[context.board.phase] = actions.toList()
        context = GameContextImpl( //{ mutableMapOf() },
            context.uuid, context.table, context.payments, context.board, newHistory, context.phasePots)
    }
}
