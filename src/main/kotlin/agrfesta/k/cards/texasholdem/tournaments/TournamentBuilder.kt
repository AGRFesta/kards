package agrfesta.k.cards.texasholdem.tournaments

import agrfesta.k.cards.playingcards.utils.RandomGenerator
import agrfesta.k.cards.playingcards.utils.SimpleRandomGenerator
import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.texasholdem.observers.TournamentObserver
import agrfesta.k.cards.texasholdem.rules.gameplay.Game
import agrfesta.k.cards.texasholdem.rules.gameplay.GameBuilder.Companion.buildingAGame
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStack
import agrfesta.k.cards.texasholdem.rules.gameplay.Table
import kotlin.properties.Delegates

typealias GameProvider = (IncreasingGamePayments, Table<PlayerStack>, GameObserver?) -> Game
typealias TournamentImplementer =
        (Set<Player>, Int, IncreasingGamePayments, (Int) -> Int, GameProvider, TournamentObserver?) -> Tournament

val defaultGameProvider: GameProvider = { payments, table, observer ->
    buildingAGame()
            .withPayments(payments)
            .withTable(table)
            .observedBy(observer)
            .build()
}

interface InitialStackStep {
    fun withAnInitialStackOf(initialStack: Int): PaymentsStep
}
interface PaymentsStep {
    fun withPayments(payments: IncreasingGamePayments): TournamentBuilder
}

class TournamentBuilder private constructor(): InitialStackStep, PaymentsStep {
    private var rndGenerator: RandomGenerator = SimpleRandomGenerator()
    private var observer: TournamentObserver? = null
    private var gameProvider = defaultGameProvider
    private var tournamentImplementation: TournamentImplementer = ::TournamentImpl
    private var buttonProvider: (Int) -> Int = { rndGenerator.nextInt(it) } // Random position

    private val subscriptions: MutableSet<Player> = mutableSetOf()

    private var initialStack by Delegates.notNull<Int>()
    private lateinit var payments: IncreasingGamePayments

    companion object {
        fun buildingTournament(): InitialStackStep = TournamentBuilder()
    }

    fun withSubscribers(vararg players: Player): TournamentBuilder {
        subscriptions.addAll(players)
        return this
    }

    fun withRandomGenerator(rndGenerator: RandomGenerator): TournamentBuilder {
        this.rndGenerator = rndGenerator
        return this
    }

    fun withButtonProvider(provider: (Int) -> Int): TournamentBuilder {
        this.buttonProvider = provider
        return this
    }

    fun observerBy(observer: TournamentObserver): TournamentBuilder {
        this.observer = observer
        return this
    }

    fun withGameProvider(provider: GameProvider): TournamentBuilder {
        this.gameProvider = provider
        return this
    }

    fun implementedBy(tournamentImplementation: TournamentImplementer): TournamentBuilder {
        this.tournamentImplementation = tournamentImplementation
        return this
    }

    override fun withAnInitialStackOf(initialStack: Int): PaymentsStep {
        this.initialStack = initialStack
        return this
    }

    override fun withPayments(payments: IncreasingGamePayments): TournamentBuilder {
        this.payments = payments
        return this
    }

    fun build(): Tournament {
        check(subscriptions.isNotEmpty()) { "Unable to create a tournament with zero players!" }
        check(subscriptions.size != 1) { "Unable to create a tournament with only one player!" }
        return tournamentImplementation.invoke(
                subscriptions, initialStack, payments, buttonProvider, gameProvider, observer)
    }

}
