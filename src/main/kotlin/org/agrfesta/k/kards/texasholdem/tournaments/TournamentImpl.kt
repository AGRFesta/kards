package org.agrfesta.k.kards.texasholdem.tournaments

import org.agrfesta.k.cards.playingcards.utils.circularIndexMapping
import org.agrfesta.k.kards.texasholdem.observers.TournamentObserver
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Player
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStack
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Table
import org.agrfesta.k.kards.texasholdem.rules.gameplay.owns
import org.agrfesta.k.kards.texasholdem.rules.gameplay.toRanking

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
            .map { player -> player owns initialStack }
            .toMutableList()

    override fun play(): List<Set<Player>> {
        var button = buttonProvider.invoke(players.size)
        while (players.size > 1) {
            playGame(button)
            payments.nextGame()
            button = players.circularIndexMapping(button-1)
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

