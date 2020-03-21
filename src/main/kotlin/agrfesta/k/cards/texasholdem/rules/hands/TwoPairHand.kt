package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.RANK_COMPARATOR
import agrfesta.kcards.playingcards.cards.Rank

class TwoPairHand (
        private val majorPairRank: Rank,
        private val minorPairRank: Rank,
        private val kicker: Rank)
    : AbstractTHHand(THPokerHand.TWO_PAIR) {

    init {
        if (majorPairRank == minorPairRank) {
            throw IllegalArgumentException("Pairs rank can't be equal: $majorPairRank")
        }
        if (RANK_COMPARATOR.compare(minorPairRank,majorPairRank) > 0) {
            throw IllegalArgumentException("Minor pair rank can't be greater than major pair: $majorPairRank,$minorPairRank")
        }
        if (majorPairRank==kicker || minorPairRank==kicker) {
            throw IllegalArgumentException("Pairs rank can't be equal to kicker rank: $kicker")
        }
    }

    override fun innerCompareTo(he: CardsEvaluation): Int {
        if (he !is TwoPairHand) {
            throw IllegalArgumentException("Comparable only to an instance of TwoPairHand")
        }
        return rawCompareTo(he)
    }

    private fun rawCompareTo(tph: TwoPairHand): Int {
        return compareBy(RANK_COMPARATOR, TwoPairHand::majorPairRank)
                .thenBy(RANK_COMPARATOR, TwoPairHand::minorPairRank)
                .thenBy(RANK_COMPARATOR, TwoPairHand::kicker)
                .compare(this, tph)
    }

    override fun toString(): String {
        return "${getHandValue()}:{$majorPairRank/$minorPairRank $kicker}"
    }
}