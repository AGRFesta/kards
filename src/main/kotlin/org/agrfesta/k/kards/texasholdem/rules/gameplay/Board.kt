package org.agrfesta.k.kards.texasholdem.rules.gameplay

import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.cards.playingcards.deck.Deck

private const val FLOP_SIZE = 3

interface Board {
    val cards: Set<Card>
    val phase: GamePhase
}
interface BoardInSequence: Board {
    fun next(): BoardInSequence
}

abstract class AbstractBoard(protected val deck: Deck, override val phase: GamePhase): BoardInSequence {
    override fun toString(): String = cards.toString()
}

enum class GamePhase {
    PRE_FLOP, FLOP, TURN, RIVER
}

class EmptyBoard(deck: Deck): AbstractBoard(deck, GamePhase.PRE_FLOP) {
    override val cards: Set<Card> = emptySet()
    override fun next(): BoardInSequence = FlopBoard(super.deck)
}
class FlopBoard(deck: Deck): AbstractBoard(deck, GamePhase.FLOP) {
    override val cards: Set<Card> = deck.draw(FLOP_SIZE).toSet()
    override fun next(): BoardInSequence = TurnBoard(deck, this)
}
class TurnBoard(deck: Deck, flop: FlopBoard): AbstractBoard(deck, GamePhase.TURN) {
    override val cards: Set<Card> = flop.cards + deck.draw()
    override fun next(): BoardInSequence = RiverBoard(deck, this)
}
class RiverBoard(deck: Deck, turn: TurnBoard): AbstractBoard(deck, GamePhase.RIVER) {
    override val cards: Set<Card> = turn.cards + deck.draw()
    override fun next(): BoardInSequence = this
}
