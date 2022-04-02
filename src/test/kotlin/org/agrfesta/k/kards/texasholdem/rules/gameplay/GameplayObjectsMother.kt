package org.agrfesta.k.kards.texasholdem.rules.gameplay

import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.cards.playingcards.deck.Deck
import org.agrfesta.k.cards.playingcards.suits.TEN
import org.agrfesta.k.cards.playingcards.suits.frenchCardsSet
import org.agrfesta.k.kards.texasholdem.DeckListImpl
import org.agrfesta.k.kards.texasholdem.observers.GameResult
import org.agrfesta.k.kards.texasholdem.observers.ShowdownPlayerResult
import org.agrfesta.k.kards.texasholdem.rules.hands.StraightHand
import org.agrfesta.k.kards.texasholdem.tournaments.IncreasingGamePayments
import org.agrfesta.k.kards.texasholdem.tournaments.LevelPayments
import org.agrfesta.k.kards.texasholdem.utils.DistinctList
import java.util.*

fun aDeck(): Deck = DeckListImpl(listOf())
fun aTable(): Table<InGamePlayer> =
    TableImpl(DistinctList.distinctListOf(anInGamePlayer(name = "A"), anInGamePlayer(name = "B")), 0u)
fun aPlayerStackTable(): Table<SittingPlayer> = TableImpl(
    DistinctList.distinctListOf(
        aSittingPlayer(alex),
        aSittingPlayer(poly)
    ), 0u)
fun aPublicInGamePlayerTable(): Table<PublicInGamePlayer> =
    TableImpl(DistinctList.distinctListOf(aPublicInGamePlayer(name = "A"), aPublicInGamePlayer(name = "B")))

fun aContext(table: Table<InGamePlayer> = aTable(), payments: GamePayments = aGamePayments())
    : MutableGameContextImpl  {
    val phasePots = emptyPhasePots<InGamePlayer, MutableMap<InGamePlayer, UInt>> { mutableMapOf() }
    return MutableGameContextImpl(uuid = UUID.randomUUID(), table =  table, payments = payments,
        board = EmptyBoard(aDeck()) as BoardInSequence, phasePots = phasePots)
}

fun aGameContext(): GameContext = aGameContextImplWith()
fun aGameContextImplWith(
    uuid: UUID = UUID.randomUUID(),
    table: Table<PublicInGamePlayer> = aPublicInGamePlayerTable(),
    payments: GamePayments = aGamePayments(),
    board: Board = board(),
    history: Map<GamePhase, List<PlayerAction>> = emptyMap(),
    phasePots: Map<GamePhase, Pot<PlayerIdentity>> = emptyMap()
) = GameContextImpl(uuid, table, payments, board, history, phasePots)
fun aGameResult() = GameResult(aPlayer(), 2000u, emptyList())
fun aShowdownPlayerResult() = ShowdownPlayerResult(anInGamePlayer(), null, aCardsEvaluation())
fun aCardsEvaluation() = StraightHand(TEN)

fun aGamePayments(): GamePayments = GamePaymentsFixedImpl(10u, 20u)
fun anIncreasingGamePayments() = IncreasingGamePayments(listOf(LevelPayments(1u, 2u)), 1u)
fun blinds(sb: UInt, bb: UInt): GamePayments = GamePaymentsFixedImpl(sb, bb)

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
