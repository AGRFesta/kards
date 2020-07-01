package agrfesta.k.cards.texasholdem.rules

import agrfesta.k.cards.texasholdem.rules.hands.*
import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.cards.Seed
import agrfesta.k.cards.playingcards.suits.*
import java.util.*

class CardsEvaluatorBaseImpl: CardsEvaluator {
    private val cardComparator: Comparator<Card> = compareBy(Card::rank)
            .thenBy(compareBy(Seed::ord), Card::seed)

    private val straightMask = intArrayOf(
            31,    //00000000011111
            62,    //00000000111110
            124,   //00000001111100
            248,   //00000011111000
            496,   //00000111110000
            992,   //00001111100000
            1984,  //00011111000000
            3968,  //00111110000000
            7936,  //01111100000000
            15872)


    override fun evaluate(set: Set<Card>): CardsEvaluation {
        checkSize(set)

        val rankRepList = getRankRepetitionsList(set)
        val histogram: List<Set<Seed>> = buildHistogram(set.sortedWith(cardComparator))

        val flush = getFlushEvaluation(groupBySeed(set))
        if (flush is FlushHand) {
            return getStraightFlushEvaluation(histogram, flush.seed) ?: flush
        }

        return getStraightEvaluation(histogram)
                ?: getFullHouseEvaluation(rankRepList)
                ?: getThreeOfAKindEvaluation(set, rankRepList)
                ?: getPairsEvaluation(set, rankRepList)
                ?: getFourOfAKindEvaluation(rankRepList)
                ?: getHighCardEvaluation(set)
    }

    private fun getRankRepetitionsList(set: Set<Card>) = set
            .groupingBy { it.rank() }
            .eachCount().entries
            .map { Pair(it.key,it.value) }
            .sortedByDescending { it.second }

    private fun groupBySeed(set: Set<Card>): Map<Seed, List<Card>> = set
            .groupingBy { it.seed() }
            .fold(  { _: Seed, _: Card -> listOf<Card>()},
                    { _, accumulator, element ->
                        accumulator.plus(element)
                    })

    private fun checkSize(cards: Set<Card>) {
        if (cards.size < 5) {
            throw IllegalArgumentException(
                    "Unable to evaluate a set of less than five cards: ${cards.size}")
        }
        if (cards.size > 7) {
            throw IllegalArgumentException(
                    "Unable to evaluate a set of more than seven cards: ${cards.size}")
        }
    }

    private fun buildHistogram(hand: Collection<Card>): List<Set<Seed>> {
        val histogram = (0 .. 12).map { HashSet<Seed>() }
        hand.forEach { histogram[it.rank().ordinal()].add(it.seed()) }
        return histogram
    }
    private fun getBitSequence(
            histogram: List<Set<Seed>>,
            predicate: (set: Set<Seed>) -> Boolean): Int {
        return histogram.indices
                .map { if (predicate(histogram[it])) 1 shl it else 0 }
                .sum() + if (predicate(histogram[0])) 1 shl histogram.size else 0
    }
    private fun getStraightRankFromSequence(bitSeq: Int): Rank? {
//        return straightMask
//                .filter { bitSeq and it == it }
//                .map { getFrenchRankFromSymbol(FrenchRank.values()[it].symbol()) }
//                .firstOrNull()

        for (i in straightMask.indices) {
            if (bitSeq and straightMask[i] == straightMask[i]) {
                return getFrenchRankFromSymbol(FrenchRank.values()[i].symbol())
            }
        }
        return null
    }
    private fun getSeedsBitSequences(histogram: List<Set<Seed>>): IntArray {
        val result = intArrayOf(0, 0, 0, 0)
        for (i in 0..histogram.size) {
            for (s in histogram[i % histogram.size]) {
                result[s.ord()] += 1 shl i
            }
        }
        return result
    }

    private fun getFlushEvaluation(groupBySeed: Map<Seed, List<Card>>): CardsEvaluation? {
        return groupBySeed.entries
                .filter { it.value.size > 4 }
                .map { createFlushEvaluation(it.key, it.value) }
                .firstOrNull()
    }
    private fun createFlushEvaluation(seed: Seed, cards: Collection<Card>): FlushHand {
        val ranks = cards.take(5).map { it.rank() }
        return FlushHand(ranks[0],ranks[1],ranks[2],ranks[3],ranks[4],seed)
    }
    private fun getStraightEvaluation(histogram: List<Set<Seed>>): CardsEvaluation? {
        val bitSeq = getBitSequence(histogram) { it.isNotEmpty() }
        val rank = getStraightRankFromSequence(bitSeq)
        return if (rank != null) {
            StraightHand(rank)
        } else null
    }
    private fun getThreeOfAKindEvaluation(
            cards: Collection<Card>,
            rankRepList: List<Pair<Rank,Int>>): CardsEvaluation? {
        if (rankRepList[0].second == 3) {
            val rank = rankRepList[0].first
            val kickers = cards
                    .filter { it.rank() != rank }
                    .map { it.rank() }
                    .sortedDescending()
            return ThreeOfAKindHand(rank, kickers[0],kickers[1])
        }
        return null
    }
    private fun getPairsEvaluation(cards: Collection<Card>,rankRepList: List<Pair<Rank,Int>>): CardsEvaluation? {
        if (rankRepList[0].second == 2 && rankRepList[1].second == 2) {
            val pairs = rankRepList
                    .filter { it.second == 2 }
                    .map { it.first }
                    .sortedDescending()
            val kicker = cards
                    .filter { it.rank()!=pairs[0] && it.rank()!=pairs[1] }
                    .map { it.rank() }
                    .max()
            return TwoPairHand(pairs[0], pairs[1], kicker!!)
        } else if (rankRepList[0].second == 2) {
            val rank = rankRepList[0].first
            val kickers = cards
                    .filter { it.rank() != rank }
                    .map { it.rank() }
                    .sortedDescending()
            return PairHand(rank, kickers[0],kickers[1],kickers[2])
        }
        return null
    }
    private fun getFullHouseEvaluation(rankRepList: List<Pair<Rank,Int>>): CardsEvaluation? {
        if (rankRepList[0].second == 3 && (rankRepList[1].second == 3 || rankRepList[1].second == 2)) {
            if (rankRepList[1].second == 2) {
                val threeRank = rankRepList[0].first
                val twoRank = rankRepList
                        .filter { it.second == 2 }
                        .map { it.first }
                        .max()!!
                return FullHouseHand(threeRank, twoRank)
            }
            if (rankRepList[1].second == 3) {
                val triples = rankRepList
                        .filter { it.second == 3 }
                        .map { it.first }
                        .sortedDescending()
                return FullHouseHand(triples[0], triples[1])
            }
        }
        return null
    }
    private fun getStraightFlushEvaluation(histogram: List<Set<Seed>>, seed: Seed): CardsEvaluation? {
        val seedsSeq = getSeedsBitSequences(histogram)
        val rank = getStraightRankFromSequence(seedsSeq[seed.ord()])
        return if (rank != null) {
            StraightFlushHand(rank, seed)
        } else null
    }
    private fun getFourOfAKindEvaluation(rankRepList: List<Pair<Rank,Int>>): CardsEvaluation? {
        if (rankRepList[0].second == 4) {
            val rank = rankRepList[0].first
            val kicker = rankRepList
                    .map { it.first }
                    .filter { it != rank}
                    .max()!!
            return FourOfAKindHand(rank, kicker)
        }
        return null
    }
    private fun getHighCardEvaluation(cards: Collection<Card>): CardsEvaluation {
        val kickers = cards
                .map { it.rank() }
                .sortedDescending()
        return HighCardHand(kickers[0],kickers[1],kickers[2],kickers[3],kickers[4])
    }
}