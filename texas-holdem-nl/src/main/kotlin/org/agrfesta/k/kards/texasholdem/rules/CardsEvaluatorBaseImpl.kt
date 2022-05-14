package org.agrfesta.k.kards.texasholdem.rules

import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.cards.playingcards.cards.Rank
import org.agrfesta.k.cards.playingcards.cards.Seed
import org.agrfesta.k.cards.playingcards.suits.frenchRankList
import org.agrfesta.k.cards.playingcards.suits.getFrenchRankFromSymbol
import org.agrfesta.k.kards.texasholdem.rules.hands.FlushHand
import org.agrfesta.k.kards.texasholdem.rules.hands.PairHand
import org.agrfesta.k.kards.texasholdem.rules.hands.StraightFlushHand
import org.agrfesta.k.kards.texasholdem.rules.hands.StraightHand
import org.agrfesta.k.kards.texasholdem.rules.hands.TwoPairHand
import org.agrfesta.k.kards.texasholdem.rules.hands.findFourOfAKindEvaluation
import org.agrfesta.k.kards.texasholdem.rules.hands.findFullHouseEvaluation
import org.agrfesta.k.kards.texasholdem.rules.hands.findThreeOfAKindEvaluation
import org.agrfesta.k.kards.texasholdem.rules.hands.getHighCardEvaluation
import org.agrfesta.k.kards.texasholdem.utils.COUNT_FOUR
import org.agrfesta.k.kards.texasholdem.utils.FIFTH_POS
import org.agrfesta.k.kards.texasholdem.utils.FIRST_POS
import org.agrfesta.k.kards.texasholdem.utils.FOURTH_POS
import org.agrfesta.k.kards.texasholdem.utils.POKER_HAND_SIZE
import org.agrfesta.k.kards.texasholdem.utils.SECOND_POS
import org.agrfesta.k.kards.texasholdem.utils.THIRD_POS

@Suppress("MagicNumber")
private val straightMask = intArrayOf(
    0b00000000011111,
    0b00000000111110,
    0b00000001111100,
    0b00000011111000,
    0b00000111110000,
    0b00001111100000,
    0b00011111000000,
    0b00111110000000,
    0b01111100000000,
    0b11111000000000)

class CardsEvaluatorBaseImpl : CardsEvaluator {

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
        return getFrenchRankFromSymbol(frenchRankList[i].symbol)
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
    val ranks = cards
            .map { it.rank }
            .sortedDescending()
            .take(POKER_HAND_SIZE)
    return FlushHand(
        ranks[FIRST_POS],
        ranks[SECOND_POS],
        ranks[THIRD_POS],
        ranks[FOURTH_POS],
        ranks[FIFTH_POS],
        seed
    )
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
              .filter { it.rank != pairs[0] && it.rank != pairs[1] }
              .map { it.rank }
              .maxOrNull()
          TwoPairHand(pairs[0], pairs[1], kicker!!)
        }
        rankRepList[0].count == 2 -> {
          val rank = rankRepList[0].rank
          val kickers = cards
              .filter { it.rank != rank }
              .map { it.rank }
              .sortedDescending()
          PairHand(rank, kickers[0], kickers[1], kickers[2])
        }
        else -> null
      }

  private fun getStraightFlushEvaluation(histogram: List<Set<Seed>>, seed: Seed): CardsEvaluation? {
    val seedsSeq = getSeedsBitSequences(histogram)
    val rank = getStraightRankFromSequence(seedsSeq[seed.ordinal])
    return if (rank != null) {
      StraightFlushHand(rank, seed)
    } else null
  }

}

data class RankCount(val rank: Rank, val count: Int)
