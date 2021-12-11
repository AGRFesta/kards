package org.agrfesta.k.kards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.deck.Deck
import agrfesta.k.cards.playingcards.suits.Suit.FRENCH
import org.agrfesta.k.kards.texasholdem.observers.GameObserver
import org.agrfesta.k.kards.texasholdem.observers.ShowdownObserver
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluatorBaseImpl
import org.agrfesta.k.kards.texasholdem.utils.UuidProvider
import java.util.*

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
    private var implementation: (MutableGameContextImpl, DealerFactory, Showdown, GameObserver?) -> Game = ::GameImpl

    private var uuidProvider: UuidProvider = { UUID.randomUUID() }

    private lateinit var payments: GamePayments
    private lateinit var table: Table<PlayerStack>

    companion object {
        fun buildingAGame(): PaymentsStep = GameBuilder()
    }

    fun withDeck(deck: Deck): GameBuilder {
        this.deck = deck
        return this
    }
    fun observedBy(observer: GameObserver?): GameBuilder {
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

    fun withUuidProvider(uuidProvider: UuidProvider): GameBuilder {
        this.uuidProvider = uuidProvider
        return this
    }

    fun implementedBy(
        implementation: (MutableGameContextImpl, dealerFactory: DealerFactory, Showdown, GameObserver?) -> Game)
        : GameBuilder {
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
        val phasePots = emptyPhasePots<InGamePlayer, MutableMap<InGamePlayer, Int>> { mutableMapOf() }
        val context = MutableGameContextImpl(uuid = uuidProvider.invoke(), table =  inGameTable, payments = payments,
            board = EmptyBoard(deck) as BoardInSequence, phasePots = phasePots)
        return implementation(context, dealerFactory, showdownProvider(observer), observer)
    }

}
