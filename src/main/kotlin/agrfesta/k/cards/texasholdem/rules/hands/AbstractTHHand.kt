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
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractTHHand

        if (handValue != other.handValue) return false

        return true
    }
    override fun hashCode(): Int {
        return handValue.hashCode()
    }

}