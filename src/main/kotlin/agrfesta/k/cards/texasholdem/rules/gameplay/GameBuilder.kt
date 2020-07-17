package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.texasholdem.observers.ShowdownObserver
import agrfesta.k.cards.texasholdem.rules.CardsEvaluatorBaseImpl
import agrfesta.k.cards.playingcards.deck.Deck
import agrfesta.k.cards.playingcards.suits.createFrenchDeck
import agrfesta.k.cards.texasholdem.observers.DealerObserver

//TODO refactoring moving default values in constructor
class GameBuilder {
    private var deck: Deck = createFrenchDeck()
    private var observer: GameObserver? = null

    private var showdownProvider: (ShowdownObserver?) -> Showdown = { ShowdownImpl(CardsEvaluatorBaseImpl(),it) }

    private var preFlopDealerProvider: (GameContext, DealerObserver?) -> Dealer = { gc, obs -> PreFlopDealer(gc,obs) }
    private var dealerProvider: (MutableMap<GamePlayer,Int>, GameContext, DealerObserver?) -> Dealer =
            { pot,context,observer -> PostFlopDealer(pot,context,observer) }
    private var implementation: (GamePayments, Deck, Table, (GameContext, DealerObserver?) -> Dealer,
                                 (MutableMap<GamePlayer,Int>, GameContext, DealerObserver?) -> Dealer,
                                 Showdown, GameObserver?) -> Game = ::GameImpl

    fun deck(deck: Deck): GameBuilder {
        this.deck = deck
        return this
    }
    fun observer(observer: GameObserver): GameBuilder {
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
    fun dealerProvider(dealerProvider: (MutableMap<GamePlayer,Int>, GameContext, DealerObserver?) -> Dealer)
            : GameBuilder {
        this.dealerProvider = dealerProvider
        return this
    }
    fun implementation(implementation: (GamePayments, Deck, Table,
                                        (GameContext, DealerObserver?) -> Dealer,
                                        (MutableMap<GamePlayer,Int>, GameContext, DealerObserver?) -> Dealer,
                                        Showdown, GameObserver?) -> Game): GameBuilder {
        this.implementation = implementation
        return this
    }

    fun build(payments: GamePayments, table: Table) =
            implementation.invoke(payments, deck, table, preFlopDealerProvider, dealerProvider,
                    showdownProvider.invoke(observer), observer)

}