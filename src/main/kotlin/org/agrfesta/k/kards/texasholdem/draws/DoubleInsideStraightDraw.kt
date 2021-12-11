package org.agrfesta.k.kards.texasholdem.draws

import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.suits.FIVE
import org.agrfesta.k.kards.texasholdem.utils.FOUR_OFF
import org.agrfesta.k.kards.texasholdem.utils.ONE_OFF
import org.agrfesta.k.kards.texasholdem.utils.THREE_OFF
import org.agrfesta.k.kards.texasholdem.utils.TWO_OFF

/*
    A double inside straight draw, or double gutshot draw or double belly buster draw can occur when either of two
    ranks will make a straight, but both are "inside" draws. For example in Texas Hold'em when holding 9-J hole cards
    on a 7-10-K flop. The probability of catching an out for a double inside straight draw is the same as for an
    outside straight draw.
 */

data class DoubleInsideStraightDraw(
        val innerTop: Rank,
        val potentialTop: Rank) : Draw {

    init {
        require(innerTop >= FIVE) { "The minimum Double Inside Straight Draw inner top is FIVE, inner top: $innerTop" }
        require(potentialTop == (innerTop + TWO_OFF) || potentialTop == (innerTop + THREE_OFF))
        { "If the inner top is $innerTop the potential top have to be ${innerTop + TWO_OFF} " +
                "or ${innerTop + THREE_OFF} but is $potentialTop" }
    }

    override fun toString(): String {
        val maxPotTop = innerTop + THREE_OFF
        val r0 = (innerTop - FOUR_OFF).symbol()
        val r1 = (innerTop - TWO_OFF).symbol()
        val r2 = (innerTop - ONE_OFF).symbol()
        val r4 = (innerTop + TWO_OFF).symbol()
        val r5 = if (potentialTop == maxPotTop) " ${maxPotTop.symbol()}" else ""
        return "[$r0 * $r1 $r2 ${innerTop.symbol()} * $r4$r5]"
    }

}
