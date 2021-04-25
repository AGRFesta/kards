package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.observers.DealerObserver
import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.texasholdem.observers.GameResult
import agrfesta.k.cards.texasholdem.observers.multipleDealerObserverOf
import agrfesta.k.cards.texasholdem.playercontext.PlayerAction
import java.util.*

interface Game {
    fun getId(): UUID
    fun play(): List<PlayerStack>
}

class GameImpl(
        private var context: GameContext<InGamePlayer>,
        private val dealerFactory: DealerFactory,
        private val showdown: Showdown,
        private val observer: GameObserver?
) : Game, DealerObserver {
    private var pot = buildPot()

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
        showdown.execute(pot, context.board)

        return context.table.players.toPlayerStack()
    }

    private fun findWinner(players: List<InGamePlayer>): InGamePlayer? {
        val winner = players.findWinner()
        if (winner != null) {
            observer?.notifyWinner( GameResult(winner.player, pot.amount(), players.toPlayerStack() ))
            winner.receive(pot.amount())
        }
        return winner
    }

    private fun findPreFlopWinner(): InGamePlayer? {
        observer?.notifyStartingPhase(context)
        val dealer = dealerFactory.preFlopDealer(context, multipleDealerObserverOf(this, observer))
        pot = dealer.collectPot()
        return findWinner(context.table.players)
    }

    private fun findWinner(): InGamePlayer? {
        context = context.nextPhase()
        observer?.notifyStartingPhase(context)
        val dealer = dealerFactory.postFlopDealer(pot, context, multipleDealerObserverOf(this, observer))
        pot = pot + dealer.collectPot()
        return findWinner(context.table.players)
    }

    override fun notifyActions(phase: GamePhase, actions: List<PlayerAction>) {
        context = context.add(actions)
    }
}
