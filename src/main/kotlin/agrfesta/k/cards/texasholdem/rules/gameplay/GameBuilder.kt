package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.observers.GameObserver
import agrfesta.k.cards.texasholdem.observers.ShowdownObserver
import agrfesta.k.cards.texasholdem.rules.CardsEvaluatorBaseImpl
import agrfesta.kcards.playingcards.deck.Deck
import agrfesta.kcards.playingcards.suits.createFrenchDeck

class GameBuilder {
    private var deck: Deck = createFrenchDeck()
    private var observer: GameObserver? = null

    //private var showdownObserved: ((ShowdownObserver) -> Showdown)? = { ShowdownImpl(CardsEvaluatorBaseImpl(),it) }
    private var showdownProvider: (ShowdownObserver?) -> Showdown = { ShowdownImpl(CardsEvaluatorBaseImpl(),it) }

    private var preFlopDealerProvider: (GameContext) -> Dealer = { PreFlopDealer(it) }
    private var dealerProvider: (MutableMap<GamePlayer,Int>, GameContext) -> Dealer =
            { pot,context -> PostFlopDealer(pot,context) }
    private var implementation: (GamePayments, Deck, Table, (GameContext) -> Dealer,
                                 (MutableMap<GamePlayer,Int>, GameContext) -> Dealer,
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

    fun preFlopDealerProvider(preFlopDealerProvider: (GameContext) -> Dealer): GameBuilder {
        this.preFlopDealerProvider = preFlopDealerProvider
        return this
    }
    fun dealerProvider(dealerProvider: (MutableMap<GamePlayer,Int>, GameContext) -> Dealer): GameBuilder {
        this.dealerProvider = dealerProvider
        return this
    }
    fun implementation(implementation: (GamePayments, Deck, Table,
                                        (GameContext) -> Dealer,
                                        (MutableMap<GamePlayer,Int>, GameContext) -> Dealer,
                                        Showdown, GameObserver?) -> Game): GameBuilder {
        this.implementation = implementation
        return this
    }

    fun build(payments: GamePayments, table: Table) =
            implementation.invoke(payments, deck, table, preFlopDealerProvider, dealerProvider,
                    showdownProvider.invoke(observer), observer)

}