package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.deck.Deck

private const val FLOP_SIZE = 3

interface Board {
    fun cards(): Set<Card>
    fun phase(): GamePhase
}
interface BoardInSequence: Board {
    fun next(): BoardInSequence
}

abstract class AbstractBoard(protected val deck: Deck, private val phase: GamePhase): BoardInSequence {
    override fun phase() = phase
}

enum class GamePhase {
    PRE_FLOP, FLOP, TURN, RIVER
}

class EmptyBoard(deck: Deck): AbstractBoard(deck, GamePhase.PRE_FLOP) {
    override fun cards(): Set<Card> = emptySet()
    override fun toString(): String = "[]"
    override fun next(): BoardInSequence = FlopBoard(super.deck)
    //override fun info() = BoardInfo(cards(), GamePhase.PRE_FLOP)
}
class FlopBoard(deck: Deck): AbstractBoard(deck, GamePhase.FLOP) {
    val flop: Set<Card> = deck.draw(FLOP_SIZE).toSet()
    override fun cards(): Set<Card> = flop
    override fun toString(): String = flop.toString()
    override fun next(): BoardInSequence = TurnBoard(deck, this)
    //override fun info() = BoardInfo(cards(), GamePhase.FLOP)
}
class TurnBoard(deck: Deck, private val flop: FlopBoard): AbstractBoard(deck, GamePhase.TURN) {
    fun flop(): Set<Card> = flop.flop
    val turn: Card = deck.draw()
    override fun cards(): Set<Card> = flop.cards() + turn
    override fun toString(): String = "$flop[$turn]"
    override fun next(): BoardInSequence = RiverBoard(deck, this)
    //override fun info() = BoardInfo(cards(), GamePhase.TURN)
}
class RiverBoard(deck: Deck, private val turn: TurnBoard): AbstractBoard(deck, GamePhase.RIVER) {
    fun flop(): Set<Card> = turn.flop()
    fun turn(): Card = turn.turn
    val river: Card = deck.draw()
    override fun cards(): Set<Card> = turn.cards() + river
    override fun toString(): String = "$turn[$river]"
    override fun next(): BoardInSequence = this
    //override fun info() = BoardInfo(cards(), GamePhase.RIVER)
}

//class BoardInfo(val cards: Set<Card>, val phase: GamePhase)
