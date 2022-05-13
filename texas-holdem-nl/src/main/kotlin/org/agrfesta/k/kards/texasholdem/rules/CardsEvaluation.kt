package org.agrfesta.k.kards.texasholdem.rules

import org.agrfesta.k.kards.texasholdem.rules.hands.THPokerHand

interface CardsEvaluation: Comparable<CardsEvaluation> {
    fun getHandValue(): THPokerHand
}
