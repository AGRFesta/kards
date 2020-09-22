package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.OrderedRankListComparator
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand.HIGH_CARD
import agrfesta.k.cards.texasholdem.utils.FIFTH_POS
import agrfesta.k.cards.texasholdem.utils.FIRST_POS
import agrfesta.k.cards.texasholdem.utils.FOURTH_POS
import agrfesta.k.cards.texasholdem.utils.POKER_HAND_SIZE
import agrfesta.k.cards.texasholdem.utils.SECOND_POS
import agrfesta.k.cards.texasholdem.utils.THIRD_POS
import kotlinx.collections.immutable.toImmutableList

class HighCardHand(
    firstKicker: Rank, secondKicker: Rank, thirdKicker: Rank, fourthKicker: Rank, fifthKicker: Rank)
  : AbstractTHHand(HIGH_CARD) {

  init {
    val set = setOf(firstKicker, secondKicker, thirdKicker, fourthKicker, fifthKicker)
    if (set.size != POKER_HAND_SIZE) {
      throw IllegalArgumentException("Multiple kickers with same Rank: " +
          "$firstKicker,$secondKicker,$thirdKicker,$fourthKicker,$fifthKicker")
    }
  }

  val kickers = listOf(firstKicker, secondKicker, thirdKicker, fourthKicker, fifthKicker)
      .sorted().reversed()
      .toImmutableList()

  override fun innerCompareTo(ce: CardsEvaluation): Int {
    if (ce !is HighCardHand) {
      throw IllegalArgumentException("Comparable only to an instance of HighCardHand")
    }
    return compareBy(OrderedRankListComparator(), HighCardHand::kickers)
        .compare(this, ce)
  }

  override fun toString(): String {
    return "${getHandValue()}:{$kickers}"
  }

}

fun getHighCardEvaluation(cards: Collection<Card>): CardsEvaluation {
  val kickers = cards
          .map { it.rank() }
          .sortedDescending()
  return HighCardHand(
          kickers[FIRST_POS],
          kickers[SECOND_POS],
          kickers[THIRD_POS],
          kickers[FOURTH_POS],
          kickers[FIFTH_POS])
}
