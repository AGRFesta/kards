package agrfesta.k.cards.texasholdem.tournaments

import agrfesta.k.cards.texasholdem.rules.gameplay.Game
import agrfesta.k.cards.texasholdem.rules.gameplay.GameBuilder
import agrfesta.k.cards.texasholdem.rules.gameplay.Table

class TournamentBuilder {
    private val subscriptions: Set<PlayerSubscription> = mutableSetOf()
    private val gameProvider: (IncreasingGamePayments, Table) -> Game =
            { payments,table -> GameBuilder().build(payments,table) }

    fun build(initialStack: Int,payments: IncreasingGamePayments): Tournament =
            Tournament(subscriptions,initialStack,payments,gameProvider)

}