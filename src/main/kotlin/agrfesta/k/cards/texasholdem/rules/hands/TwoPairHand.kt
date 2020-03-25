package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.kcards.playingcards.cards.Rank
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand.TWO_PAIR

class TwoPairHand (
        private val majorPairRank: Rank,
        private val minorPairRank: Rank,
        private val kicker: Rank)
    : AbstractTHHand(TWO_PAIR) {

    init {
        if (majorPairRank == minorPairRank) {
            throw IllegalArgumentException("Pairs rank can't be equal: $majorPairRank")
        }
        if (minorPairRank > majorPairRank) {
            throw IllegalArgumentException("Minor pair rank can't be greater than major pair: $majorPairRank,$minorPairRank")
        }
        if (majorPairRank==kicker || minorPairRank==kicker) {
            throw IllegalArgumentException("Pairs rank can't be equal to kicker rank: $kicker")
        }
    }

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        if (ce !is TwoPairHand) {
            throw IllegalArgumentException("Comparable only to an instance of TwoPairHand")
        }
        return compareBy(TwoPairHand::majorPairRank)
                .thenBy(TwoPairHand::minorPairRank)
                .thenBy(TwoPairHand::kicker)
                .compare(this, ce)
    }

    override fun toString(): String {
        return "${getHandValue()}:{$majorPairRank/$minorPairRank $kicker}"
    }
}