package org.agrfesta.k.kards.texasholdem.cli.evaluationreport

import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.cards.playingcards.suits.Suit

class AllPossibleHandsIterator(private val handSize: Int): Iterator<Set<Card>> {
    private val cards = Suit.FRENCH.cards.toList()
    private val n = cards.size
    private var gotFirst = false
    private val pointers = MutableList(handSize) { it }

    private fun updatePointer(p: Int) {
        pointers[p]++
        if (pointers[p] >= n-(handSize-p-1)) {
            if (p == 0) throw Exception()
            updatePointer(p - 1)
            pointers[p] = pointers[p-1] + 1
        }
    }

    override fun hasNext(): Boolean {
        if (!gotFirst) {
            gotFirst = true
            return true
        }
        return try {
            updatePointer(handSize - 1)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun next(): Set<Card> = pointers.map { cards[it] }.toSet()

}
