package agrfesta.k.cards.texasholdem

interface HandEvaluation: Comparable<HandEvaluation> {
    fun getHandValue(): THPokerHand
    fun isValid(): Boolean
}