package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.OrderedRankListComparator
import agrfesta.kcards.playingcards.cards.Rank
import kotlinx.collections.immutable.toImmutableList

class PairHand (
        private val pairRank: Rank,
        firstKicker: Rank, secondKicker: Rank, thirdKicker: Rank)
    : AbstractTHHand(THPokerHand.PAIR) {

    init {
        val set = setOf(firstKicker, secondKicker, thirdKicker)
        if (set.size != 3) {
            throw IllegalArgumentException("Multiple kickers with same Rank: $firstKicker,$secondKicker,$thirdKicker")
        }
        if (set.contains(pairRank)) {
            throw IllegalArgumentException("Rank of kickers can't be equal to pairRank: $pairRank")
        }
    }

    val kickers = listOf(firstKicker, secondKicker, thirdKicker)
            .sorted().reversed()
            .toImmutableList()

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        if (ce !is PairHand) {
            throw IllegalArgumentException("Comparable only to an instance of PairHand")
        }
        return rawCompareTo(ce)
    }

    private fun rawCompareTo(ph: PairHand): Int {
        return compareBy(PairHand::pairRank)
                .thenBy(OrderedRankListComparator(), PairHand::kickers)
                .compare(this, ph)
    }

    override fun toString(): String {
        return "${getHandValue()}:{$pairRank $kickers}"
    }
}