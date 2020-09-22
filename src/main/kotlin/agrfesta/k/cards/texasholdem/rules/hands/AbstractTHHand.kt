package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation

abstract class AbstractTHHand(private val handValue: THPokerHand) : CardsEvaluation {

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
    //TODO test this hashcode, maybe using a Set
    override fun hashCode(): Int {
        return handValue.hashCode()
    }

}
