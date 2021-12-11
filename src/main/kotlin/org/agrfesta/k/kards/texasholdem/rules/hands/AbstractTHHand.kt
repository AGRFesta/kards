package org.agrfesta.k.kards.texasholdem.rules.hands

import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation

abstract class AbstractTHHand(private val handValue: org.agrfesta.k.kards.texasholdem.rules.hands.THPokerHand) :
    org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation {

    protected abstract fun innerCompareTo(ce: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation): Int

    override fun compareTo(other: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation): Int {
        val res = handValue.compareTo(other.getHandValue())
        return if (res == 0) {
            innerCompareTo(other)
        } else res
    }

    override fun getHandValue(): org.agrfesta.k.kards.texasholdem.rules.hands.THPokerHand = handValue

    override fun equals(other: Any?): Boolean {
        if (other !is org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation) return false
        return compareTo(other) == 0
    }

    override fun hashCode(): Int {
        return handValue.hashCode()
    }

}
