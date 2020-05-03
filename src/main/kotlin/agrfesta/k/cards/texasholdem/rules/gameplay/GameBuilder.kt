package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.rules.CardsEvaluatorBaseImpl
import agrfesta.kcards.playingcards.deck.Deck
import agrfesta.kcards.playingcards.suits.createFrenchDeck

class GameBuilder {
    private var deck: Deck = createFrenchDeck()
    private var showdown: Showdown = ShowdownImpl(CardsEvaluatorBaseImpl())
    private var preFlopDealerProvider: (GameContext) -> Dealer = { PreFlopDealer(it) }
    private var dealerProvider: (MutableMap<GamePlayer,Int>, GameContext) -> Dealer =
            { pot,context -> PostFlopDealer(pot,context) }
    private var implementation: (GamePayments, Deck, Table, (GameContext) -> Dealer,
                                 (MutableMap<GamePlayer,Int>, GameContext) -> Dealer, Showdown) -> Game = ::GameImpl

    fun deck(deck: Deck): GameBuilder {
        this.deck = deck
        return this
    }
    fun showdown(showdown: Showdown): GameBuilder {
        this.showdown = showdown
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
    fun implementation(implementation: (GamePayments, Deck, Table, (GameContext) -> Dealer, (MutableMap<GamePlayer,Int>, GameContext) -> Dealer, Showdown) -> Game): GameBuilder {
        this.implementation = implementation
        return this
    }

    fun build(payments: GamePayments, table: Table) =
            implementation.invoke(payments, deck, table, preFlopDealerProvider, dealerProvider, showdown)

}