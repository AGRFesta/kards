package agrfesta.k.cards.texasholdem

interface CardsEvaluation: Comparable<CardsEvaluation> {
    fun getHandValue(): THPokerHand
}