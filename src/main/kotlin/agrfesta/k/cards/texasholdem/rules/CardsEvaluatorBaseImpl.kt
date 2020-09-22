package agrfesta.k.cards.texasholdem.rules

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.cards.Seed
import agrfesta.k.cards.playingcards.suits.FrenchRank
import agrfesta.k.cards.playingcards.suits.getFrenchRankFromSymbol
import agrfesta.k.cards.texasholdem.rules.hands.FlushHand
import agrfesta.k.cards.texasholdem.rules.hands.PairHand
import agrfesta.k.cards.texasholdem.rules.hands.StraightFlushHand
import agrfesta.k.cards.texasholdem.rules.hands.StraightHand
import agrfesta.k.cards.texasholdem.rules.hands.TwoPairHand
import agrfesta.k.cards.texasholdem.rules.hands.findFourOfAKindEvaluation
import agrfesta.k.cards.texasholdem.rules.hands.findFullHouseEvaluation
import agrfesta.k.cards.texasholdem.rules.hands.findThreeOfAKindEvaluation
import agrfesta.k.cards.texasholdem.rules.hands.getHighCardEvaluation
import agrfesta.k.cards.texasholdem.utils.COUNT_FOUR
import agrfesta.k.cards.texasholdem.utils.FIFTH_POS
import agrfesta.k.cards.texasholdem.utils.FIRST_POS
import agrfesta.k.cards.texasholdem.utils.FOURTH_POS
import agrfesta.k.cards.texasholdem.utils.POKER_HAND_SIZE
import agrfesta.k.cards.texasholdem.utils.SECOND_POS
import agrfesta.k.cards.texasholdem.utils.THIRD_POS

private const val M_00000000011111 = 31
private const val M_00000000111110 = 62
private const val M_00000001111100 = 124
private const val M_00000011111000 = 248
private const val M_00000111110000 = 496
private const val M_00001111100000 = 992
private const val M_00011111000000 = 1984
private const val M_00111110000000 = 3968
private const val M_01111100000000 = 7936
private const val M_11111000000000 = 15872

class CardsEvaluatorBaseImpl : CardsEvaluator {

  private val straightMask = intArrayOf(
      M_00000000011111,
      M_00000000111110,
      M_00000001111100,
      M_00000011111000,
      M_00000111110000,
      M_00001111100000,
      M_00011111000000,
      M_00111110000000,
      M_01111100000000,
      M_11111000000000)


  override fun evaluate(set: Set<Card>): CardsEvaluation {
    checkSize(set)

    val rankRepList = getRankRepetitionsList(set)
    val histogram: List<Set<Seed>> = buildHistogram(set.sortedWith(cardComparator))

    val flush = getFlushEvaluation(groupBySeed(set))
    if (flush is FlushHand) {
      return getStraightFlushEvaluation(histogram, flush.seed) ?: flush
    }

    return getStraightEvaluation(histogram)
        ?: findFullHouseEvaluation(rankRepList)
        ?: findThreeOfAKindEvaluation(set, rankRepList)
        ?: getPairsEvaluation(set, rankRepList)
        ?: findFourOfAKindEvaluation(rankRepList)
        ?: getHighCardEvaluation(set)
  }

  private fun getStraightRankFromSequence(bitSeq: Int): Rank? {
    for (i in straightMask.indices) {
      if (bitSeq and straightMask[i] == straightMask[i]) {
        return getFrenchRankFromSymbol(FrenchRank.values()[i].symbol())
      }
    }
    return null
  }

  private fun getFlushEvaluation(groupBySeed: Map<Seed, List<Card>>): CardsEvaluation? {
    return groupBySeed.entries
        .filter { it.value.size > COUNT_FOUR }
        .map { createFlushEvaluation(it.key, it.value) }
        .firstOrNull()
  }

  private fun createFlushEvaluation(seed: Seed, cards: Collection<Card>): FlushHand {
    val ranks = cards.take(POKER_HAND_SIZE).map { it.rank() }
    return FlushHand(ranks[FIRST_POS], ranks[SECOND_POS], ranks[THIRD_POS], ranks[FOURTH_POS], ranks[FIFTH_POS], seed)
  }

  private fun getStraightEvaluation(histogram: List<Set<Seed>>): CardsEvaluation? {
    val bitSeq = getBitSequence(histogram) { it.isNotEmpty() }
    val rank = getStraightRankFromSequence(bitSeq)
    return if (rank != null) {
      StraightHand(rank)
    } else null
  }

  private fun getPairsEvaluation(cards: Collection<Card>, rankRepList: List<RankCount>): CardsEvaluation? =
      when {
        rankRepList[0].count == 2 && rankRepList[1].count == 2 -> {
          val pairs = rankRepList
              .filter { it.count == 2 }
              .map { it.rank }
              .sortedDescending()
          val kicker = cards
              .filter { it.rank() != pairs[0] && it.rank() != pairs[1] }
              .map { it.rank() }
              .max()
          TwoPairHand(pairs[0], pairs[1], kicker!!)
        }
        rankRepList[0].count == 2 -> {
          val rank = rankRepList[0].rank
          val kickers = cards
              .filter { it.rank() != rank }
              .map { it.rank() }
              .sortedDescending()
          PairHand(rank, kickers[0], kickers[1], kickers[2])
        }
        else -> null
      }

  private fun getStraightFlushEvaluation(histogram: List<Set<Seed>>, seed: Seed): CardsEvaluation? {
    val seedsSeq = getSeedsBitSequences(histogram)
    val rank = getStraightRankFromSequence(seedsSeq[seed.ord()])
    return if (rank != null) {
      StraightFlushHand(rank, seed)
    } else null
  }

}

data class RankCount(val rank: Rank, val count: Int)
