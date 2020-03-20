package agrfesta.k.cards.texasholdem

import agrfesta.kcards.playingcards.cards.Rank
import kotlinx.collections.immutable.toImmutableList

//TODO doc
class OnePairHand (
        private val pairRank: Rank,
        firstKicker: Rank, secondKicker: Rank, thirdKicker: Rank)
    : AbstractTHHand(THPokerHand.ONE_PAIR) {

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
            .sortedWith(compareBy(Rank::ord))
            .toImmutableList()

    override fun innerCompareTo(he: CardsEvaluation): Int {
        if (he !is OnePairHand) {
            throw IllegalArgumentException("Comparable only to an instance of OnePairEvaluation")
        }
        return rawCompareTo(he)
    }

    private fun rawCompareTo(ope: OnePairHand): Int {
        return compareBy(RANK_COMPARATOR, OnePairHand::pairRank)
                .thenBy(OrderedRankListComparator(), OnePairHand::kickers)
                .compare(this, ope)
    }

    override fun toString(): String {
        return "${getHandValue()}:{$pairRank $kickers}"
    }
}