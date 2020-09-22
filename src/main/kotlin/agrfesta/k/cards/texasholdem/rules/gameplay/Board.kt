package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.deck.Deck

private const val FLOP_SIZE = 3

interface Board {
    fun cards(): Set<Card>
    fun next(): Board
    fun info(): BoardInfo
}

enum class GamePhase {
    PRE_FLOP, FLOP, TURN, RIVER
}

class EmptyBoard(private val deck: Deck): Board {
    override fun cards(): Set<Card> = setOf()
    override fun toString(): String = "[]"
    override fun next(): Board = FlopBoard(deck)
    override fun info() = BoardInfo(cards(), GamePhase.PRE_FLOP)
}
class FlopBoard(private val deck: Deck): Board {
    val flop: Set<Card> = deck.draw(FLOP_SIZE).toSet()
    override fun cards(): Set<Card> = flop
    override fun toString(): String = flop.toString()
    override fun next(): Board = TurnBoard(deck, this)
    override fun info() = BoardInfo(cards(), GamePhase.FLOP)
}
class TurnBoard(private val deck: Deck, private val flop: FlopBoard): Board {
    fun flop(): Set<Card> = flop.flop
    val turn: Card = deck.draw()
    override fun cards(): Set<Card> = flop.cards() + turn
    override fun toString(): String = "$flop[$turn]"
    override fun next(): Board = RiverBoard(deck, this)
    override fun info() = BoardInfo(cards(), GamePhase.TURN)
}
class RiverBoard(deck: Deck, private val turn: TurnBoard): Board {
    fun flop(): Set<Card> = turn.flop()
    fun turn(): Card = turn.turn
    val river: Card = deck.draw()
    override fun cards(): Set<Card> = turn.cards() + river
    override fun toString(): String = "$turn[$river]"
    override fun next(): Board = this
    override fun info() = BoardInfo(cards(), GamePhase.RIVER)
}

class BoardInfo(val cards: Set<Card>, val phase: GamePhase)
