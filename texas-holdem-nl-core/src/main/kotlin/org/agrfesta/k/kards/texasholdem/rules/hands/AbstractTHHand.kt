package org.agrfesta.k.kards.texasholdem.rules.hands

import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation

abstract class AbstractTHHand(private val handValue: THPokerHand): CardsEvaluation {

    protected abstract fun innerCompareTo(ce: CardsEvaluation): Int

    override fun compareTo(other: CardsEvaluation): Int {
        val res = handValue.compareTo(other.getHandValue())
        return if (res == 0) {
            innerCompareTo(other)
        } else res
    }

    override fun getHandValue(): THPokerHand = handValue

    override fun equals(other: Any?): Boolean {
        if (other !is CardsEvaluation) return false
        return compareTo(other) == 0
    }

    override fun hashCode(): Int {
        return handValue.hashCode()
    }

}
