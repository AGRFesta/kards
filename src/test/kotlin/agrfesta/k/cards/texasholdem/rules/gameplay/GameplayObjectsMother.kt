package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.deck.Deck
import agrfesta.k.cards.playingcards.suits.frenchCardsSet
import agrfesta.k.cards.texasholdem.DeckListImpl
import java.util.*

fun aDeck(): Deck = DeckListImpl(listOf())
fun aTable(): Table<InGamePlayer> = Table(listOf(anInGamePlayer(),anInGamePlayer()), 0)

fun aContext(table: Table<InGamePlayer> = aTable(), payments: GamePayments = aGamePayments()): InGameContext =
        GameContextImpl(UUID.randomUUID(), table, payments, EmptyBoard(aDeck()))

fun aGamePayments(): GamePayments = GamePaymentsFixedImpl(10, 20)
fun blinds(sb: Int, bb: Int): GamePayments = GamePaymentsFixedImpl(sb, bb)

fun board(vararg strings: String): Board = object : Board {
    override val cards: Set<Card> = frenchCardsSet(*strings)
    override val phase: GamePhase  = when (cards().size) {
        0 -> GamePhase.PRE_FLOP
        3 -> GamePhase.FLOP
        4 -> GamePhase.TURN
        5 -> GamePhase.RIVER
        else -> throw IllegalArgumentException("Unable to get phase from invalid board, cards:${cards()}")
    }
}
