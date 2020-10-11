package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.deck.Deck
import agrfesta.k.cards.texasholdem.observers.DealerObserver
import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.texasholdem.playercontext.PlayerAction

interface Game {
    fun play()
}

class GameImpl(
        initialContext: GameContext,
        private val deck: Deck,
        private val dealerFactory: DealerFactory,
        private val showdown: Showdown,
        private val observer: GameObserver?
) : Game, DealerObserver {
    private var pot = buildPot()
    private var context = initialContext

    override fun play() {
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
    }

    private fun findWinner(players: List<InGamePlayer>): InGamePlayer? {
        val winner = players.findWinner()
        if (winner != null) {
            observer?.notifyWinner(winner.player, pot.amount())
            winner.receive(pot.amount())
        }
        return winner
    }

    private fun findPreFlopWinner(): InGamePlayer? {
        observer?.notifyStartingPhase(context.board)
        context.table.players.forEach { it.cards = deck.draw(2).toSet() }
        val dealer = dealerFactory.preFlopDealer(context, this)
        pot = dealer.collectPot()
        return findWinner(context.table.players)
    }

    private fun findWinner(): InGamePlayer? {
        context = context.nextPhase()
        observer?.notifyStartingPhase(context.board)
        val dealer = dealerFactory.postFlopDealer(pot, context, this)
        pot = pot + dealer.collectPot()
        return findWinner(context.table.players)
    }

    override fun notifyActions(phase: GamePhase, actions: List<PlayerAction>) {
        //TODO maybe check that phase is coherent with actual context
        context = context.add(actions)
    }
}
