package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.OrderedRankListComparator
import agrfesta.kcards.playingcards.cards.Rank
import kotlinx.collections.immutable.toImmutableList

class HighCardHand (
        firstKicker: Rank, secondKicker: Rank, thirdKicker: Rank, fourthKicker: Rank, fifthKicker: Rank)
    : AbstractTHHand(THPokerHand.HIGH_CARD) {

    init {
        val set = setOf(firstKicker, secondKicker, thirdKicker, fourthKicker, fifthKicker)
        if (set.size != 5) {
            throw IllegalArgumentException("Multiple kickers with same Rank: $firstKicker,$secondKicker,$thirdKicker,$fourthKicker,$fifthKicker")
        }
    }

    val kickers = listOf(firstKicker, secondKicker, thirdKicker, fourthKicker, fifthKicker)
            .sorted().reversed()
            .toImmutableList()

    override fun innerCompareTo(he: CardsEvaluation): Int {
        if (he !is HighCardHand) {
            throw IllegalArgumentException("Comparable only to an instance of HighCardHand")
        }
        return rawCompareTo(he)
    }

    private fun rawCompareTo(hcd: HighCardHand): Int {
        return compareBy(OrderedRankListComparator(), HighCardHand::kickers)
                .compare(this, hcd)
    }

    override fun toString(): String {
        return "${getHandValue()}:{$kickers}"
    }

}