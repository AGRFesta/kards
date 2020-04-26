package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.DeckListImpl
import agrfesta.kcards.playingcards.deck.Deck

fun aDeck(): Deck = DeckListImpl(listOf())
fun aTable(): Table = Table(listOf(aPlayer(),aPlayer()), 0)

fun aContext(): GameContext = GameContext(aTable(), aGamePayments(), EmptyBoard(aDeck()))
fun aContext(table: Table, payments: GamePayments): GameContext =
        GameContext(table, payments, EmptyBoard(aDeck()))

fun aGamePayments(): GamePayments = GamePaymentsFixedImpl(10, 20)
fun blinds(sb: Int, bb: Int): GamePayments = GamePaymentsFixedImpl(sb, bb)