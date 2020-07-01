package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.deck.Deck

interface Board {
    fun cards(): Set<Card>
    fun next(): Board
}

class EmptyBoard(private val deck: Deck): Board {
    override fun cards(): Set<Card> = setOf()
    override fun toString(): String = "[]"
    override fun next(): Board = FlopBoard(deck)
}
class FlopBoard(private val deck: Deck): Board {
    val flop: Set<Card> = deck.draw(3).toSet()
    override fun cards(): Set<Card> = flop
    override fun toString(): String = flop.toString()
    override fun next(): Board = TurnBoard(deck, this)
}
class TurnBoard(private val deck: Deck, private val flop: FlopBoard): Board {
    fun flop(): Set<Card> = flop.flop
    val turn: Card = deck.draw()
    override fun cards(): Set<Card> = flop.cards() + turn
    override fun toString(): String = "$flop[$turn]"
    override fun next(): Board = RiverBoard(deck, this)
}
class RiverBoard(deck: Deck, private val turn: TurnBoard): Board {
    fun flop(): Set<Card> = turn.flop()
    fun turn(): Card = turn.turn
    val river: Card = deck.draw()
    override fun cards(): Set<Card> = turn.cards() + river
    override fun toString(): String = "$turn[$river]"
    override fun next(): Board = this
}