package agrfesta.k.cards.texasholdem.tournaments

import agrfesta.k.cards.texasholdem.observers.TournamentObserver
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStack
import agrfesta.k.cards.texasholdem.rules.gameplay.Table
import agrfesta.k.cards.texasholdem.rules.gameplay.toRanking
import agrfesta.k.cards.texasholdem.utils.circularPos

interface Tournament {
    fun play(): List<Set<Player>>
}

class TournamentImpl(subscriptions: Set<Player>,
                     private val initialStack: Int,
                     private val payments: IncreasingGamePayments,
                     private val buttonProvider: (Int) -> Int,
                     private val gameProvider: GameProvider,
                     private val observer: TournamentObserver? ): Tournament {
    private val losers: MutableList<Set<Player>> = mutableListOf()
    private var players: List<PlayerStack> = subscriptions
            .map { player -> PlayerStack(player,initialStack) }
            .toMutableList()

    override fun play(): List<Set<Player>> {
        var button = buttonProvider.invoke(players.size)
        while (players.size > 1) {
            playGame(button)
            payments.nextGame()
            button = circularPos(button-1, players.size)
        }
        val winner = listOf(setOf(players[0].player))
        return winner + losers.reversed()
    }

    private fun playGame(button: Int) {
        val table = Table(players,button)
        val game = gameProvider.invoke(payments, table, observer)
        val postGamePlayers = game.play()
        removeLosers(postGamePlayers)
        observer?.notifyTournamentRanking(players.toRanking(), losers.reversed())
    }

    private fun removeLosers(postGamePlayers: List<PlayerStack>) {
        val playersOutOfChips = postGamePlayers
                .filter { it.stack == 0 }
                .map { it.player }

        losers.addAll( playersOutOfChips
                .map { initialStack(it) to it }
                .groupingBy { it.first }
                .fold(setOf<Player>()) { set, entry -> set.plus(entry.second)}
                .entries.sortedBy { it.key }
                .map { it.value }
        )

        // Remove losers
        players = postGamePlayers.filter { it.stack > 0 }
    }
    private fun initialStack(player: Player): Int {
        val playerStack: PlayerStack? = players.firstOrNull { it.player == player }
        requireNotNull(playerStack) { "Trying to remove a player without a starting stack" }
        return playerStack.stack
    }

}

