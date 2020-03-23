package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.OrderedRankListComparator
import agrfesta.kcards.playingcards.cards.Rank
import kotlinx.collections.immutable.toImmutableList

class ThreeOfAKindHand(
        private val tokRank: Rank,
        firstKicker: Rank, secondKicker: Rank)
    : AbstractTHHand(THPokerHand.THREE_OF_A_KIND) {

    init {
        if (firstKicker == secondKicker) {
            throw IllegalArgumentException("Kickers have same Rank: $firstKicker,$secondKicker")
        }
        if (tokRank==firstKicker || tokRank==secondKicker) {
            throw IllegalArgumentException("Rank of kickers can't be equal to tokRank: $tokRank")
        }
    }

    val kickers = listOf(firstKicker, secondKicker)
            .sorted().reversed()
            .toImmutableList()

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        if (ce !is ThreeOfAKindHand) {
            throw IllegalArgumentException("Comparable only to an instance of ThreeOfAKindHand")
        }
        return rawCompareTo(ce)
    }
    private fun rawCompareTo(tokh: ThreeOfAKindHand): Int {
        return compareBy(ThreeOfAKindHand::tokRank)
                .thenBy(OrderedRankListComparator(), ThreeOfAKindHand::kickers)
                .compare(this, tokh)
    }

    override fun toString(): String = "${getHandValue()}:{$tokRank $kickers}"
}