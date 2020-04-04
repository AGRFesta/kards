package agrfesta.k.cards.texasholdem.draws

import agrfesta.kcards.playingcards.cards.Rank
import agrfesta.kcards.playingcards.suits.FrenchRank
import agrfesta.kcards.playingcards.suits.SIX
import agrfesta.kcards.playingcards.suits.getFrenchRankFromSymbol

/*
    A double inside straight draw, or double gutshot draw or double belly buster draw can occur when either of two
    ranks will make a straight, but both are "inside" draws. For example in Texas Hold'em when holding 9-J hole cards
    on a 7-10-K flop. The probability of catching an out for a double inside straight draw is the same as for an
    outside straight draw.
 */

class DoubleInsideStraightDraw(
        private val innerTop: Rank,
        private val potentialTop: Rank): Draw {

    init {
        if (innerTop < SIX) {
            throw IllegalArgumentException("The minimum Double Inside Straight Draw inner top is SIX, inner top: $innerTop")
        }
        val minPotTop = getFrenchRankFromSymbol(FrenchRank.values()[innerTop.ordinal()-2].symbol()) //TODO KISS it
        val maxPotTop = getFrenchRankFromSymbol(FrenchRank.values()[innerTop.ordinal()-3].symbol()) //TODO KISS it
        if (potentialTop!=minPotTop && potentialTop!=maxPotTop) {
            throw IllegalArgumentException(
                    "If the inner top is $innerTop the potential top have to be $minPotTop or $maxPotTop but is $potentialTop")
        }
    }

}