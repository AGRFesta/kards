package org.agrfesta.k.kards.texasholdem.rules

import org.agrfesta.k.kards.texasholdem.rules.hands.THPokerHand

interface CardsEvaluation: Comparable<org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation> {
    fun getHandValue(): THPokerHand
}
