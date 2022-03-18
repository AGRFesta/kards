package org.agrfesta.k.kards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.deck.Deck
import agrfesta.k.cards.playingcards.suits.TEN
import agrfesta.k.cards.playingcards.suits.frenchCardsSet
import org.agrfesta.k.kards.texasholdem.DeckListImpl
import org.agrfesta.k.kards.texasholdem.observers.GameResult
import org.agrfesta.k.kards.texasholdem.observers.ShowdownPlayerResult
import org.agrfesta.k.kards.texasholdem.rules.hands.StraightHand
import java.util.*

fun aDeck(): Deck = DeckListImpl(listOf())
fun aTable(): Table<InGamePlayer> = Table(listOf(anInGamePlayer(),anInGamePlayer()), 0)
fun aPlayerStackTable(): Table<PlayerStack> = Table(listOf(aPlayerStack(),aPlayerStack()), 0)
fun anOpponentsTable(): Table<Opponent> = Table(listOf(anOpponent(),anOpponent()), 0)

fun aContext(table: Table<InGamePlayer> = aTable(), payments: GamePayments = aGamePayments())
    : MutableGameContextImpl  {
    val phasePots = emptyPhasePots<InGamePlayer, MutableMap<InGamePlayer, Int>> { mutableMapOf() }
    return MutableGameContextImpl(uuid = UUID.randomUUID(), table =  table, payments = payments,
        board = EmptyBoard(aDeck()) as BoardInSequence, phasePots = phasePots)
}

fun aGameContext(): GameContext = aGameContextImplWith()
fun aGameContextImplWith(
    uuid: UUID = UUID.randomUUID(),
    table: Table<Opponent> = anOpponentsTable(),
    payments: GamePayments = aGamePayments(),
    board: Board = board(),
    history: Map<GamePhase, List<PlayerAction>> = emptyMap(),
    phasePots: Map<GamePhase, Pot<SeatName>> = emptyMap()
) = GameContextImpl(uuid, table, payments, board, history, phasePots)
fun aGameResult() = GameResult(aPlayer(), 2000, emptyList())
fun aShowdownPlayerResult() = ShowdownPlayerResult(anInGamePlayer(), null, aCardsEvaluation())
fun aCardsEvaluation() = StraightHand(TEN)

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
fun aBoardAt(phase: GamePhase): Board =  object : Board {
    override val cards: Set<Card> = emptySet() // we don't care about cards here
    override val phase: GamePhase = phase
}
