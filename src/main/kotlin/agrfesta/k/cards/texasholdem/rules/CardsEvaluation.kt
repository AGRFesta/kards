package agrfesta.k.cards.texasholdem.rules

import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand

interface CardsEvaluation: Comparable<CardsEvaluation> {
    fun getHandValue(): THPokerHand
}
