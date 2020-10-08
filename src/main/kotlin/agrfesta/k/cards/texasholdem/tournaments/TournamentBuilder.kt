package agrfesta.k.cards.texasholdem.tournaments

import agrfesta.k.cards.playingcards.utils.RandomGenerator
import agrfesta.k.cards.playingcards.utils.SimpleRandomGenerator
import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.texasholdem.observers.TournamentObserver
import agrfesta.k.cards.texasholdem.rules.gameplay.Game
import agrfesta.k.cards.texasholdem.rules.gameplay.GameBuilder
import agrfesta.k.cards.texasholdem.rules.gameplay.InGamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.Table

class TournamentBuilder(private val rndGenerator: RandomGenerator,
                        defaultGameProvider: (IncreasingGamePayments, Table<InGamePlayer>, GameObserver?) -> Game,
                        private val tournamentImplementation: (Set<Player>, Int, IncreasingGamePayments,
                                                               (Int) -> Int,
                                                               (IncreasingGamePayments, Table<InGamePlayer>, GameObserver?) -> Game,
                                                               TournamentObserver?) -> Tournament) {
  private var observer: TournamentObserver? = null

  private val subscriptions: MutableSet<Player> = mutableSetOf()

  private var buttonProvider: (Int) -> Int = { rndGenerator.nextInt(it) } // Random position
  private var gameProvider = defaultGameProvider

  fun subscriptions(vararg players: Player): TournamentBuilder {
    subscriptions.addAll(players)
    return this
  }

  fun buttonProvider(provider: (Int) -> Int): TournamentBuilder {
    this.buttonProvider = provider
    return this
  }

  fun observer(observer: TournamentObserver): TournamentBuilder {
    this.observer = observer
    return this
  }

  fun gameProvider(provider: (IncreasingGamePayments, Table<InGamePlayer>, GameObserver?) -> Game): TournamentBuilder {
    this.gameProvider = provider
    return this
  }

  fun build(initialStack: Int, payments: IncreasingGamePayments): Tournament {
    if (subscriptions.isEmpty()) throw IllegalStateException("Unable to create a tournament with zero players!")
    if (subscriptions.size == 1) throw IllegalStateException("Unable to create a tournament with only one player!")
    return tournamentImplementation.invoke(
        subscriptions, initialStack, payments, buttonProvider, gameProvider, observer)
  }

}

fun tournamentBuilder() = TournamentBuilder(SimpleRandomGenerator(), { payments, table, _ ->
  GameBuilder().build(payments, table)
}, { subscriptions, initialStack, payments, buttonProvider, gameProvider, observer ->
  TournamentImpl(subscriptions, initialStack, payments, buttonProvider, gameProvider, observer)
})
