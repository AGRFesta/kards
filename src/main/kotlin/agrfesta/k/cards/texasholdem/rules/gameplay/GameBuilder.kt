package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.deck.Deck
import agrfesta.k.cards.playingcards.suits.Suit.FRENCH
import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.texasholdem.observers.ShowdownObserver
import agrfesta.k.cards.texasholdem.rules.CardsEvaluatorBaseImpl

interface PaymentsStep {
    fun withPayments(payments: GamePayments): TableStep
    fun withPayments(sb: Int, bb: Int, ante: Int? = null): TableStep
}
interface TableStep {
    fun withTable(table: Table<PlayerStack>): GameBuilder
}

class GameBuilder private constructor(): PaymentsStep, TableStep {
    private var deck: Deck = FRENCH.createDeck()
    private var observer: GameObserver? = null

    private var showdownProvider: (ShowdownObserver?) -> Showdown = { ShowdownImpl(CardsEvaluatorBaseImpl(),it) }

    private var dealerFactory: DealerFactory = DealerFactoryImpl()
    private var implementation: (GameContext, DealerFactory, Showdown, GameObserver?) -> Game = ::GameImpl

    private lateinit var payments: GamePayments
    private lateinit var table: Table<PlayerStack>

    companion object {
        fun buildingAGame(): PaymentsStep = GameBuilder()
    }

    fun withDeck(deck: Deck): GameBuilder {
        this.deck = deck
        return this
    }
    fun observedBy(observer: GameObserver): GameBuilder {
        this.observer = observer
        return this
    }

    fun showdown(showdown: Showdown): GameBuilder {
        this.showdownProvider = { showdown }
        return this
    }
    fun showdown(showdownProvider: (ShowdownObserver?) -> Showdown): GameBuilder {
        this.showdownProvider = showdownProvider
        return this
    }

    fun withDealerFactory(dealerFactory: DealerFactory): GameBuilder {
        this.dealerFactory = dealerFactory
        return this
    }

    fun implementedBy(implementation: (GameContext, dealerFactory: DealerFactory,
                                       Showdown, GameObserver?) -> Game): GameBuilder {
        this.implementation = implementation
        return this
    }

    override fun withPayments(payments: GamePayments): TableStep {
        this.payments = payments
        return this
    }
    override fun withPayments(sb: Int, bb: Int, ante: Int?): TableStep {
        this.payments = GamePaymentsFixedImpl(sb, bb, ante)
        return this
    }

    override fun withTable(table: Table<PlayerStack>): GameBuilder {
        this.table = table
        return this
    }

    fun build(): Game {
        val inGameTable = table.map { InGamePlayer(it.player, it.stack, deck.draw(2).toSet()) }
        val context = GameContext(inGameTable, payments, EmptyBoard(deck), mapOf())
        return implementation.invoke(context, dealerFactory, showdownProvider.invoke(observer), observer)
    }

}
