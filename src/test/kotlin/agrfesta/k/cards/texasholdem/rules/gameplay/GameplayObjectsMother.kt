package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.DeckListImpl
import agrfesta.kcards.playingcards.cards.Card
import agrfesta.kcards.playingcards.deck.Deck
import agrfesta.kcards.playingcards.suits.frenchCardsSet

fun aDeck(): Deck = DeckListImpl(listOf())
fun aTable(): Table = Table(listOf(aPlayer(),aPlayer()), 0)

fun aContext(): GameContext = GameContext(aTable(), aGamePayments(), EmptyBoard(aDeck()))
fun aContext(table: Table, payments: GamePayments): GameContext =
        GameContext(table, payments, EmptyBoard(aDeck()))

fun aGamePayments(): GamePayments = GamePaymentsFixedImpl(10, 20)
fun blinds(sb: Int, bb: Int): GamePayments = GamePaymentsFixedImpl(sb, bb)

fun board(vararg strings: String): Board = object : Board {
    override fun cards(): Set<Card> = frenchCardsSet(*strings)
    override fun next(): Board = this
}