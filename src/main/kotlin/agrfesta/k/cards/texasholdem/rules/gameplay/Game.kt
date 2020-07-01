package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.playingcards.deck.Deck

interface Game {
    fun play()
}

class GameImpl(
        private val payments: GamePayments,
        private val deck: Deck,
        private val table: Table,
        private val preFlopDealerProvider: (GameContext) -> Dealer,
        private val dealerProvider: (MutableMap<GamePlayer,Int>, GameContext) -> Dealer,
        private val showdown: Showdown,
        private val observer: GameObserver?
): Game {
    private var pot = buildPot()
    private var board: Board = EmptyBoard(deck)

    override fun play() {
        if (findPreFlopWinner() != null) return

        // Flop
        if (findWinner() != null) return

        // Turn
        if (findWinner() != null) return

        // River
        if (findWinner() != null) return

        // Showdown
        showdown.execute(pot, board)
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
        observer?.notifyStartingPhase(board)
        table.players.forEach { it.cards = deck.draw(2).toSet() }
        val dealer = preFlopDealerProvider.invoke(GameContext(table, payments, board))
        pot = dealer.collectPot()
        return findWinner(table.players)
    }

    private fun findWinner(): GamePlayer? {
        board = board.next()
        observer?.notifyStartingPhase(board)
        val dealer = dealerProvider.invoke(pot, GameContext(table, payments, board))
        pot = pot + dealer.collectPot()
        return findWinner(table.players)
    }
}