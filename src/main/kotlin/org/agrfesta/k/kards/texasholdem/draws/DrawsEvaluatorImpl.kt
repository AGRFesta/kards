package org.agrfesta.k.kards.texasholdem.draws

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.cards.Seed
import agrfesta.k.cards.playingcards.suits.ACE
import agrfesta.k.cards.playingcards.suits.EIGHT
import agrfesta.k.cards.playingcards.suits.FIVE
import agrfesta.k.cards.playingcards.suits.FOUR
import agrfesta.k.cards.playingcards.suits.JACK
import agrfesta.k.cards.playingcards.suits.KING
import agrfesta.k.cards.playingcards.suits.NINE
import agrfesta.k.cards.playingcards.suits.QUEEN
import agrfesta.k.cards.playingcards.suits.SEVEN
import agrfesta.k.cards.playingcards.suits.SIX
import agrfesta.k.cards.playingcards.suits.TEN
import agrfesta.k.cards.playingcards.suits.THREE
import agrfesta.k.cards.playingcards.suits.TWO
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluatorBaseImpl
import org.agrfesta.k.kards.texasholdem.rules.buildHistogram
import org.agrfesta.k.kards.texasholdem.rules.cardComparator
import org.agrfesta.k.kards.texasholdem.rules.getBitSequence
import org.agrfesta.k.kards.texasholdem.rules.groupBySeed
import org.agrfesta.k.kards.texasholdem.rules.hands.THPokerHand
import org.agrfesta.k.kards.texasholdem.utils.FIRST_POS
import org.agrfesta.k.kards.texasholdem.utils.FOURTH_POS
import org.agrfesta.k.kards.texasholdem.utils.SECOND_POS
import org.agrfesta.k.kards.texasholdem.utils.THIRD_POS

const val FLUSH_DRAW_SIZE = 4

private const val M_00000010111011 = 187
private const val M_00000101110110 = 374
private const val M_00001011101100 = 748
private const val M_00010111011000 = 1496
private const val M_00101110110000 = 2992
private const val M_01011101100000 = 5984
private const val M_10111011000000 = 11968

private const val M_00000001011101 = 93
private const val M_00000010111010 = 186
private const val M_00000101110100 = 372
private const val M_00001011101000 = 744
private const val M_00010111010000 = 1488
private const val M_00101110100000 = 2976
private const val M_01011101000000 = 5952
private const val M_10111010000000 = 11904

private const val M_00000000011110 = 30
private const val M_00000000111100 = 60
private const val M_00000001111000 = 120
private const val M_00000011110000 = 240
private const val M_00000111100000 = 480
private const val M_00001111000000 = 960
private const val M_00011110000000 = 1920
private const val M_00111100000000 = 3840
private const val M_01111000000000 = 7680

private const val M_00000000011101 = 29
private const val M_00000000011011 = 27
private const val M_00000000010111 = 23
private const val M_00000000001111 = 15
private const val M_00000000111010 = 58
private const val M_00000000110110 = 54
private const val M_00000000101110 = 46
private const val M_00000001110100 = 116
private const val M_00000001101100 = 108
private const val M_00000001011100 = 92
private const val M_00000011101000 = 232
private const val M_00000011011000 = 216
private const val M_00000010111000 = 184
private const val M_00000111010000 = 464
private const val M_00000110110000 = 432
private const val M_00000101110000 = 368
private const val M_00001110100000 = 928
private const val M_00001101100000 = 864
private const val M_00001011100000 = 736
private const val M_00011101000000 = 1856
private const val M_00011011000000 = 1728
private const val M_00010111000000 = 1472
private const val M_00111010000000 = 3712
private const val M_00110110000000 = 3456
private const val M_00101110000000 = 2944
private const val M_01110100000000 = 7424
private const val M_01101100000000 = 6912
private const val M_01011100000000 = 5888
private const val M_11110000000000 = 15360
private const val M_11101000000000 = 14848
private const val M_11011000000000 = 13824
private const val M_10111000000000 = 11776

class DrawsEvaluatorImpl : DrawsEvaluator {
  private val handEvaluator: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluator = CardsEvaluatorBaseImpl()

  private val masks = listOf(
      StraightDrawMask(M_00000010111011, DoubleInsideStraightDraw(JACK, ACE)),
      StraightDrawMask(M_00000101110110, DoubleInsideStraightDraw(TEN, KING)),
      StraightDrawMask(M_00001011101100, DoubleInsideStraightDraw(NINE, QUEEN)),
      StraightDrawMask(M_00010111011000, DoubleInsideStraightDraw(EIGHT, JACK)),
      StraightDrawMask(M_00101110110000, DoubleInsideStraightDraw(SEVEN, TEN)),
      StraightDrawMask(M_01011101100000, DoubleInsideStraightDraw(SIX, NINE)),
      StraightDrawMask(M_10111011000000, DoubleInsideStraightDraw(FIVE, EIGHT)),

      StraightDrawMask(M_00000001011101, DoubleInsideStraightDraw(QUEEN, ACE)),
      StraightDrawMask(M_00000010111010, DoubleInsideStraightDraw(JACK, KING)),
      StraightDrawMask(M_00000101110100, DoubleInsideStraightDraw(TEN, QUEEN)),
      StraightDrawMask(M_00001011101000, DoubleInsideStraightDraw(NINE, JACK)),
      StraightDrawMask(M_00010111010000, DoubleInsideStraightDraw(EIGHT, TEN)),
      StraightDrawMask(M_00101110100000, DoubleInsideStraightDraw(SEVEN, NINE)),
      StraightDrawMask(M_01011101000000, DoubleInsideStraightDraw(SIX, EIGHT)),
      StraightDrawMask(M_10111010000000, DoubleInsideStraightDraw(FIVE, SEVEN)),

      StraightDrawMask(M_00000000011110, OutsideStraightDraw(KING)),
      StraightDrawMask(M_00000000111100, OutsideStraightDraw(QUEEN)),
      StraightDrawMask(M_00000001111000, OutsideStraightDraw(JACK)),
      StraightDrawMask(M_00000011110000, OutsideStraightDraw(TEN)),
      StraightDrawMask(M_00000111100000, OutsideStraightDraw(NINE)),
      StraightDrawMask(M_00001111000000, OutsideStraightDraw(EIGHT)),
      StraightDrawMask(M_00011110000000, OutsideStraightDraw(SEVEN)),
      StraightDrawMask(M_00111100000000, OutsideStraightDraw(SIX)),
      StraightDrawMask(M_01111000000000, OutsideStraightDraw(FIVE)),

      StraightDrawMask(M_00000000011101, InsideStraightDraw(ACE, KING)),
      StraightDrawMask(M_00000000011011, InsideStraightDraw(ACE, QUEEN)),
      StraightDrawMask(M_00000000010111, InsideStraightDraw(ACE, JACK)),
      StraightDrawMask(M_00000000001111, InsideStraightDraw(ACE, TEN)),
      StraightDrawMask(M_00000000111010, InsideStraightDraw(KING, QUEEN)),
      StraightDrawMask(M_00000000110110, InsideStraightDraw(KING, JACK)),
      StraightDrawMask(M_00000000101110, InsideStraightDraw(KING, TEN)),
      StraightDrawMask(M_00000001110100, InsideStraightDraw(QUEEN, JACK)),
      StraightDrawMask(M_00000001101100, InsideStraightDraw(QUEEN, TEN)),
      StraightDrawMask(M_00000001011100, InsideStraightDraw(QUEEN, NINE)),
      StraightDrawMask(M_00000011101000, InsideStraightDraw(JACK, TEN)),
      StraightDrawMask(M_00000011011000, InsideStraightDraw(JACK, NINE)),
      StraightDrawMask(M_00000010111000, InsideStraightDraw(JACK, EIGHT)),
      StraightDrawMask(M_00000111010000, InsideStraightDraw(TEN, NINE)),
      StraightDrawMask(M_00000110110000, InsideStraightDraw(TEN, EIGHT)),
      StraightDrawMask(M_00000101110000, InsideStraightDraw(TEN, SEVEN)),
      StraightDrawMask(M_00001110100000, InsideStraightDraw(NINE, EIGHT)),
      StraightDrawMask(M_00001101100000, InsideStraightDraw(NINE, SEVEN)),
      StraightDrawMask(M_00001011100000, InsideStraightDraw(NINE, SIX)),
      StraightDrawMask(M_00011101000000, InsideStraightDraw(EIGHT, SEVEN)),
      StraightDrawMask(M_00011011000000, InsideStraightDraw(EIGHT, SIX)),
      StraightDrawMask(M_00010111000000, InsideStraightDraw(EIGHT, FIVE)),
      StraightDrawMask(M_00111010000000, InsideStraightDraw(SEVEN, SIX)),
      StraightDrawMask(M_00110110000000, InsideStraightDraw(SEVEN, FIVE)),
      StraightDrawMask(M_00101110000000, InsideStraightDraw(SEVEN, FOUR)),
      StraightDrawMask(M_01110100000000, InsideStraightDraw(SIX, FIVE)),
      StraightDrawMask(M_01101100000000, InsideStraightDraw(SIX, FOUR)),
      StraightDrawMask(M_01011100000000, InsideStraightDraw(SIX, THREE)),
      StraightDrawMask(M_11110000000000, InsideStraightDraw(FIVE, FIVE)),
      StraightDrawMask(M_11101000000000, InsideStraightDraw(FIVE, FOUR)),
      StraightDrawMask(M_11011000000000, InsideStraightDraw(FIVE, THREE)),
      StraightDrawMask(M_10111000000000, InsideStraightDraw(FIVE, TWO))

  )

  override fun evaluate(set: Set<Card>, ce: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation?): Set<Draw> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun evaluate(set: Set<Card>): Set<Draw> {
    val hand = handEvaluator.evaluate(set)
    if (hand.getHandValue() >= THPokerHand.FLUSH) return setOf()

    val histogram: List<Set<Seed>> = buildHistogram(set.sortedWith(cardComparator))
    val draws = mutableSetOf<Draw>()

    val flushDraw = findFlushDraw(groupBySeed(set))
    if (flushDraw != null) draws.add(flushDraw)

    val straightDraw = findStraightDraw(histogram)
    if (straightDraw != null) draws.add(straightDraw)

    return draws
  }

  private fun findStraightDraw(histogram: List<Set<Seed>>): Draw? {
    val bitSeq = getBitSequence(histogram) { it.isNotEmpty() }
    return findStraightDrawFromSequence(bitSeq)
  }

  private fun findStraightDrawFromSequence(bitSeq: Int): Draw? {
    return masks
        .filter { bitSeq and it.value == it.value }
        .map { it.draw }
        .firstOrNull()
  }

  private fun findFlushDraw(groupBySeed: Map<Seed, List<Card>>): Draw? {
    return groupBySeed.entries
        .filter { it.value.size == FLUSH_DRAW_SIZE }
        .map { createFlushDraw(it.key, it.value) }
        .firstOrNull()
  }

  private fun createFlushDraw(seed: Seed, cards: Collection<Card>): FlushDraw {
    val ranks = cards.take(FLUSH_DRAW_SIZE).map { it.rank() }
    return FlushDraw(ranks[FIRST_POS], ranks[SECOND_POS], ranks[THIRD_POS], ranks[FOURTH_POS], seed)
  }
}

private data class StraightDrawMask(val value: Int, val draw: Draw)

