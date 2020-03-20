package agrfesta.k.cards.texasholdem

abstract class AbstractTHHand(private val handValue: THPokerHand) : CardsEvaluation {

    protected abstract fun innerCompareTo(he: CardsEvaluation): Int

    override fun compareTo(he: CardsEvaluation): Int {
        val res = handValue.compareTo(he.getHandValue())
        return if (res == 0) {
            innerCompareTo(he)
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