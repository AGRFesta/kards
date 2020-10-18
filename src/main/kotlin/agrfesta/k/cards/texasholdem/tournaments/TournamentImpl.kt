package agrfesta.k.cards.texasholdem.tournaments

import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.texasholdem.observers.GameResult
import agrfesta.k.cards.texasholdem.observers.ShowdownPlayerResult
import agrfesta.k.cards.texasholdem.observers.TournamentObserver
import agrfesta.k.cards.texasholdem.observers.multipleGameObserversOf
import agrfesta.k.cards.texasholdem.observers.toRanking
import agrfesta.k.cards.texasholdem.rules.gameplay.Board
import agrfesta.k.cards.texasholdem.rules.gameplay.InGamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus
import agrfesta.k.cards.texasholdem.rules.gameplay.Table
import agrfesta.k.cards.texasholdem.utils.circularPos

interface Tournament {
    fun play(): List<Set<Player>>
}

class TournamentImpl(subscriptions: Set<Player>,
                     private val initialStack: Int,
                     private val payments: IncreasingGamePayments,
                     private val buttonProvider: (Int) -> Int,
                     private val gameProvider: GameProvider,
                     private val observer: TournamentObserver? ): Tournament, GameObserver {
    private val gameObservers = multipleGameObserversOf(this, observer)
    private val losers: MutableList<Set<Player>> = mutableListOf()
    private val players: MutableList<InGamePlayer> = subscriptions
            .map { player -> InGamePlayer(player,initialStack) }
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
        val initialStacks: Map<Player,Int> = players.map { it.player to it.stack }.toMap()
        val table = Table(players,button)
        val game = gameProvider.invoke(payments, table, gameObservers)
        game.play()
        removeLosers(initialStacks)
        players.forEach { it.status = PlayerStatus.NONE }
        observer?.notifyTournamentRanking(players.toRanking(), losers.reversed())
    }

    private fun removeLosers(initialStacks: Map<Player,Int>) {
        val playersOutOfChips = players
                .filter { it.stack == 0 }
                .map { it.player }
        players.removeIf { playersOutOfChips.contains(it.player) }

        losers.addAll(
                playersOutOfChips
                .map { (initialStacks[it] ?: error("Trying to remove a player without a starting stack")) to it }
                .groupingBy { it.first }
                .fold(setOf<Player>()) { set, entry -> set.plus(entry.second)}
                .entries.sortedBy { it.key }
                .map { it.value }
        )
    }

    override fun notifyWinner(result: GameResult) {
        TODO("Not yet implemented")
    }

    override fun notifyStartingPhase(board: Board) {
        TODO("Not yet implemented")
    }

    override fun notifyResult(result: Collection<ShowdownPlayerResult>) {
        TODO("Not yet implemented")
    }

}

