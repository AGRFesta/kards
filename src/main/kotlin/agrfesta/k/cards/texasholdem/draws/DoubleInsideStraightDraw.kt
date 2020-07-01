package agrfesta.k.cards.texasholdem.draws

import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.suits.FIVE

/*
    A double inside straight draw, or double gutshot draw or double belly buster draw can occur when either of two
    ranks will make a straight, but both are "inside" draws. For example in Texas Hold'em when holding 9-J hole cards
    on a 7-10-K flop. The probability of catching an out for a double inside straight draw is the same as for an
    outside straight draw.
 */

data class DoubleInsideStraightDraw(
        private val innerTop: Rank,
        private val potentialTop: Rank): Draw {

    init {
        if (innerTop < FIVE) {
            throw IllegalArgumentException("The minimum Double Inside Straight Draw inner top is FIVE, inner top: $innerTop")
        }
        if (potentialTop!=(innerTop+2) && potentialTop!=(innerTop+3)) {
            throw IllegalArgumentException(
                    "If the inner top is $innerTop the potential top have to be ${innerTop+2} or ${innerTop+3} but is $potentialTop")
        }
    }

    override fun toString(): String {
        val maxPotTop = innerTop+3
        val r0 = (innerTop-4).symbol()
        val r1 = (innerTop-2).symbol()
        val r2 = (innerTop-1).symbol()
        val r4 = (innerTop+2).symbol()
        val r5 = if (potentialTop == maxPotTop) " ${maxPotTop.symbol()}" else ""
        return "[$r0 * $r1 $r2 ${innerTop.symbol()} * $r4$r5]"
    }

}