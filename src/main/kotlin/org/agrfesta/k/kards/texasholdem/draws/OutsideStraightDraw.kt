package org.agrfesta.k.kards.texasholdem.draws

import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.suits.FIVE
import agrfesta.k.cards.playingcards.suits.KING
import org.agrfesta.k.kards.texasholdem.utils.ONE_OFF
import org.agrfesta.k.kards.texasholdem.utils.THREE_OFF
import org.agrfesta.k.kards.texasholdem.utils.TWO_OFF

/*
    An outside straight draw, also called up and down, double-ended straight draw or open-ended straight draw,
    is a hand with four of the five needed cards in sequence (and could be completed on either end) that may
    improve to a straight. For example, x-9-8-7-6-x. An outside straight draw has eight outs (four cards to
    complete the top of the straight and four cards to complete the bottom of the straight). Straight draws
    including an ace are not outside straight draws, because the straight can only be completed on one end
    (has four outs).
 */

data class OutsideStraightDraw(val top: Rank): Draw {

    init {
        require(top >= FIVE) { "The minimum Outside Straight Draw top is FIVE, top: $top" }
        require(top <= KING) { "The maximum Outside Straight Draw top is KING, top: $top" }
    }

    override fun toString(): String = StringBuilder("[* ${top.symbol()}")
            .append(" ${(top- ONE_OFF).symbol()}")
            .append(" ${(top- TWO_OFF).symbol()}")
            .append(" ${(top- THREE_OFF).symbol()} *]")
            .toString()
}
