package agrfesta.k.cards.texasholdem.tournaments

import agrfesta.k.cards.texasholdem.rules.gameplay.*

class Tournament(subscriptions: Set<PlayerSubscription>,
                 private val initialStack: Int,
                 private val payments: IncreasingGamePayments,
                 private val gameProvider: (IncreasingGamePayments, Table) -> Game) {
    private val losers: MutableList<Player> = mutableListOf()
    private val players: MutableList<GamePlayer> = subscriptions
            .map { s -> GamePlayer(s.player,initialStack){ s.strategy } }
            .toMutableList()

    fun play() {
        var button = 0 //TODO random position
        while (players.size > 1) {
            playGame(button)
            payments.nextGame()
            button = (button+1)%players.size
        }
    }

    private fun playGame(button: Int) {
        val table = Table(players,button)
        val game = gameProvider.invoke(payments,table)
        game.play()
        removeLosers()
    }

    private fun removeLosers() {
        //TODO handle players removed at same time, should be a rule for removal order
        val zeroStack = players
                .filter { it.stack == 0 }
                .map { it.player }
        players.removeIf { zeroStack.contains(it.player) }
        losers.addAll(zeroStack)
    }

}

class PlayerSubscription(val player: Player, val strategy: PlayerStrategyInterface)