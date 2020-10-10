package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.deck.Deck
import agrfesta.k.cards.playingcards.suits.Suit.FRENCH
import agrfesta.k.cards.texasholdem.observers.DealerObserver
import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.texasholdem.observers.ShowdownObserver
import agrfesta.k.cards.texasholdem.rules.CardsEvaluatorBaseImpl

interface PaymentsStep {
    fun withPayments(payments: GamePayments): TableStep
    fun withPayments(sb: Int, bb: Int, ante: Int? = null): TableStep
}
interface TableStep {
    fun withTable(table: Table<InGamePlayer>): GameBuilder
}

class GameBuilder private constructor(): PaymentsStep, TableStep {
    private var deck: Deck = FRENCH.createDeck()
    private var observer: GameObserver? = null

    private var showdownProvider: (ShowdownObserver?) -> Showdown = { ShowdownImpl(CardsEvaluatorBaseImpl(),it) }

    private var preFlopDealerProvider: (GameContext, DealerObserver?) -> Dealer = { gc, obs -> PreFlopDealer(gc,obs) }
    private var dealerProvider: (MutableMap<InGamePlayer,Int>, GameContext, DealerObserver?) -> Dealer =
            { pot,context,observer -> PostFlopDealer(pot,context,observer) }
    private var implementation: (GameContext, Deck, (GameContext, DealerObserver?) -> Dealer,
                                 (MutableMap<InGamePlayer,Int>, GameContext, DealerObserver?) -> Dealer,
                                 Showdown, GameObserver?) -> Game = ::GameImpl

    private lateinit var payments: GamePayments
    private lateinit var table: Table<InGamePlayer>

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

    fun preFlopDealerProvider(preFlopDealerProvider: (GameContext, DealerObserver?) -> Dealer): GameBuilder {
        this.preFlopDealerProvider = preFlopDealerProvider
        return this
    }
    fun dealerProvider(dealerProvider: (MutableMap<InGamePlayer,Int>, GameContext, DealerObserver?) -> Dealer)
            : GameBuilder {
        this.dealerProvider = dealerProvider
        return this
    }
    fun implementedBy(implementation: (GameContext, Deck, (GameContext, DealerObserver?) -> Dealer,
                                       (MutableMap<InGamePlayer,Int>, GameContext, DealerObserver?) -> Dealer,
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

    override fun withTable(table: Table<InGamePlayer>): GameBuilder {
        this.table = table
        return this
    }

    fun build(): Game {
        val context = GameContext(table, payments, EmptyBoard(deck), mapOf())
        return implementation.invoke(context, deck, preFlopDealerProvider, dealerProvider,
                showdownProvider.invoke(observer), observer)
    }

}
