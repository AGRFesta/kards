package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.deck.Deck
import agrfesta.k.cards.texasholdem.observers.DealerObserver
import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.texasholdem.playercontext.PlayerAction

interface Game {
    fun play()
}

class GameImpl(
        payments: GamePayments,
        private val deck: Deck,
        private val table: Table,
        private val preFlopDealerProvider: (GameContext, DealerObserver) -> Dealer,
        private val dealerProvider: (MutableMap<GamePlayer,Int>, GameContext, DealerObserver) -> Dealer,
        private val showdown: Showdown,
        private val observer: GameObserver?
): Game, DealerObserver {
    private var pot = buildPot()
    private var actualContext = GameContext(table, payments, EmptyBoard(deck), mapOf())

    override fun play() {
        if (findPreFlopWinner() != null) return

        // Flop
        if (findWinner() != null) return

        // Turn
        if (findWinner() != null) return

        // River
        if (findWinner() != null) return

        // Showdown
        showdown.execute(pot, actualContext.board)
    }

    private fun findWinner(players: List<GamePlayer>): GamePlayer? {
        val winner = players.findWinner()
        if (winner != null) {
            observer?.notifyWinner(winner.player,pot.amount())
            winner.receive(pot.amount())
        }
        return winner
    }

    private fun findPreFlopWinner(): GamePlayer? {
        observer?.notifyStartingPhase(actualContext.board)
        table.players.forEach { it.cards = deck.draw(2).toSet() }
        val dealer = preFlopDealerProvider.invoke(actualContext, this)
        pot = dealer.collectPot()
        return findWinner(table.players)
    }

    private fun findWinner(): GamePlayer? {
        actualContext = actualContext.nextPhase()
        observer?.notifyStartingPhase(actualContext.board)
        val dealer = dealerProvider.invoke(pot, actualContext, this)
        pot = pot + dealer.collectPot()
        return findWinner(table.players)
    }

    override fun notifyActions(phase: GamePhase, actions: List<PlayerAction>) {
        //TODO maybe check that phase is coherent with actual context
        actualContext = actualContext.add(actions)
    }
}