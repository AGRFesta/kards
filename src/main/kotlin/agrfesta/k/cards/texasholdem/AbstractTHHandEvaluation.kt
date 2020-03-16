package agrfesta.k.cards.texasholdem

abstract class AbstractTHHandEvaluation(private val handValue: THPokerHand) : HandEvaluation {

    protected abstract fun innerCompareTo(he: HandEvaluation?): Int

    override fun compareTo(he: HandEvaluation): Int {
        val res = handValue.compareTo(he.getHandValue())
        return if (res == 0) {
            innerCompareTo(he)
        } else res
    }

    override fun getHandValue(): THPokerHand {
        return handValue
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractTHHandEvaluation

        if (handValue != other.handValue) return false

        return true
    }

    override fun hashCode(): Int {
        return handValue.hashCode()
    }

//    override fun hashCode(): Int {
//        val prime = 31
//        var result = 1
//        result = prime * result + (handValue?.hashCode() ?: 0)
//        return result
//    }
//
//    override fun equals(obj: Any?): Boolean {
//        if (this === obj) return true
//        if (obj == null) return false
//        if (javaClass != obj.javaClass) return false
//        val other = obj as AbstractTHHandEvaluation
//        return if (handValue !== other.handValue) false else true
//    }

}