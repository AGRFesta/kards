package org.agrfesta.k.kards.texasholdem.tournaments

import org.agrfesta.k.cards.playingcards.suits.Suit
import org.agrfesta.k.cards.playingcards.utils.SimpleRandomGenerator
import org.agrfesta.k.cards.playingcards.utils.circularIndexMapping
import org.agrfesta.k.kards.texasholdem.observers.GameObserver
import org.agrfesta.k.kards.texasholdem.observers.TournamentObserver
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Game
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GameImpl
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Player
import org.agrfesta.k.kards.texasholdem.rules.gameplay.SittingPlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Table
import org.agrfesta.k.kards.texasholdem.rules.gameplay.TableImpl
import org.agrfesta.k.kards.texasholdem.rules.gameplay.owns
import org.agrfesta.k.kards.texasholdem.rules.gameplay.toRanking
import org.agrfesta.k.kards.texasholdem.utils.DistinctList.Companion.distinctListOf
import java.util.*
import kotlin.random.Random

typealias GameProvider = (IncreasingGamePayments, Table<SittingPlayer>, GameObserver?) -> Game

interface TournamentDescriptor {
    val initialStack: UInt
    val paymentsDefinition: IncreasingGamePaymentsDefinition
}

data class TournamentDescriptorImpl(
    override val initialStack: UInt,
    override val paymentsDefinition: IncreasingGamePaymentsDefinition
): TournamentDescriptor

interface Tournament: TournamentDescriptor {
    val uuid: UUID
    fun play(): List<Set<Player>>
}

private val defaultGameProvider: GameProvider = { payments, table, observer ->
    GameImpl(payments = payments, table = table, observer = observer)
}

fun gameProviderRandomBased(random: Random): GameProvider = { payments, table, observer ->
    GameImpl(payments = payments, table = table, observer = observer, deck = Suit.FRENCH.createDeck(random))
}

class TournamentImpl(
    override val uuid: UUID = UUID.randomUUID(),
    descriptor: TournamentDescriptor,
    subscriptions: Set<Player>,
    private val observer: TournamentObserver? = null,
    private val gameProvider: GameProvider = defaultGameProvider,
    private val buttonProvider: (Int) -> UInt = { SimpleRandomGenerator().nextInt(it).toUInt() } //TODO improve it
): Tournament, TournamentDescriptor by descriptor {
    private val losers: MutableList<Set<Player>> = mutableListOf()
    private var players: List<SittingPlayer>
    private val payments = descriptor.paymentsDefinition.generatePayments()

    init {
        check(subscriptions.isNotEmpty()) { "Unable to create a tournament with zero players!" }
        check(subscriptions.size != 1) { "Unable to create a tournament with only one player!" }
        players = subscriptions
            .map { player -> player owns initialStack }
            .toMutableList()
    }

    override fun play(): List<Set<Player>> {
        var button = buttonProvider(players.size)
        while (players.size > 1) {
            playGame(button)
            payments.nextGame()
            button = players.circularIndexMapping(button-1u)
        }
        val winner = listOf(setOf(players[0]))
        return winner + losers.reversed()
    }

    private fun playGame(button: UInt) {
        val table = TableImpl(distinctListOf(players),button)
        val game = gameProvider(payments, table, observer)
        val postGamePlayers = game.play()
        removeLosers(postGamePlayers)
        observer?.notifyTournamentRanking(players.toRanking(), losers.reversed())
    }

    private fun removeLosers(postGamePlayers: List<SittingPlayer>) {
        val playersOutOfChips = postGamePlayers
                .filter { it.stack == 0u }
                .map { it }

        losers.addAll( playersOutOfChips
                .map { initialStack(it) to it }
                .groupingBy { it.first }
                .fold(setOf<Player>()) { set, entry -> set.plus(entry.second)}
                .entries.sortedBy { it.key }
                .map { it.value }
        )

        // Remove losers
        players = postGamePlayers.filter { it.stack > 0u }
    }
    private fun initialStack(player: Player): UInt {
        val sittingPlayer: SittingPlayer? = players.firstOrNull { it == player }
        requireNotNull(sittingPlayer) { "Trying to remove a player without a starting stack" }
        return sittingPlayer.stack
    }

}

