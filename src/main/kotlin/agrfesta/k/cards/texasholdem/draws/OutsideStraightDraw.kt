package agrfesta.k.cards.texasholdem.draws

import agrfesta.kcards.playingcards.cards.Rank
import agrfesta.kcards.playingcards.suits.FOUR

/*
    An outside straight draw, also called up and down, double-ended straight draw or open-ended straight draw,
    is a hand with four of the five needed cards in sequence (and could be completed on either end) that may
    improve to a straight. For example, x-9-8-7-6-x. An outside straight draw has eight outs (four cards to
    complete the top of the straight and four cards to complete the bottom of the straight). Straight draws
    including an ace are not outside straight draws, because the straight can only be completed on one end
    (has four outs).
 */

class OutsideStraightDraw(private val top: Rank): Draw {

    init {
        if (top < FOUR) {
            throw IllegalArgumentException("The minimum Outside Straight Draw top is FOUR, top: $top")
        }
    }

}