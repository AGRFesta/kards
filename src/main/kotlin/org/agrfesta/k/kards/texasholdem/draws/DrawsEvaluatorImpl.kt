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

@Suppress("MagicNumber")
private val masks = listOf(
    StraightDrawMask(0b00000010111011, DoubleInsideStraightDraw(JACK, ACE)),
    StraightDrawMask(0b00000101110110, DoubleInsideStraightDraw(TEN, KING)),
    StraightDrawMask(0b00001011101100, DoubleInsideStraightDraw(NINE, QUEEN)),
    StraightDrawMask(0b00010111011000, DoubleInsideStraightDraw(EIGHT, JACK)),
    StraightDrawMask(0b00101110110000, DoubleInsideStraightDraw(SEVEN, TEN)),
    StraightDrawMask(0b01011101100000, DoubleInsideStraightDraw(SIX, NINE)),
    StraightDrawMask(0b10111011000000, DoubleInsideStraightDraw(FIVE, EIGHT)),

    StraightDrawMask(0b00000001011101, DoubleInsideStraightDraw(QUEEN, ACE)),
    StraightDrawMask(0b00000010111010, DoubleInsideStraightDraw(JACK, KING)),
    StraightDrawMask(0b00000101110100, DoubleInsideStraightDraw(TEN, QUEEN)),
    StraightDrawMask(0b00001011101000, DoubleInsideStraightDraw(NINE, JACK)),
    StraightDrawMask(0b00010111010000, DoubleInsideStraightDraw(EIGHT, TEN)),
    StraightDrawMask(0b00101110100000, DoubleInsideStraightDraw(SEVEN, NINE)),
    StraightDrawMask(0b01011101000000, DoubleInsideStraightDraw(SIX, EIGHT)),
    StraightDrawMask(0b10111010000000, DoubleInsideStraightDraw(FIVE, SEVEN)),

    StraightDrawMask(0b00000000011110, OutsideStraightDraw(KING)),
    StraightDrawMask(0b00000000111100, OutsideStraightDraw(QUEEN)),
    StraightDrawMask(0b00000001111000, OutsideStraightDraw(JACK)),
    StraightDrawMask(0b00000011110000, OutsideStraightDraw(TEN)),
    StraightDrawMask(0b00000111100000, OutsideStraightDraw(NINE)),
    StraightDrawMask(0b00001111000000, OutsideStraightDraw(EIGHT)),
    StraightDrawMask(0b00011110000000, OutsideStraightDraw(SEVEN)),
    StraightDrawMask(0b00111100000000, OutsideStraightDraw(SIX)),
    StraightDrawMask(0b01111000000000, OutsideStraightDraw(FIVE)),

    StraightDrawMask(0b00000000011101, InsideStraightDraw(ACE, KING)),
    StraightDrawMask(0b00000000011011, InsideStraightDraw(ACE, QUEEN)),
    StraightDrawMask(0b00000000010111, InsideStraightDraw(ACE, JACK)),
    StraightDrawMask(0b00000000001111, InsideStraightDraw(ACE, TEN)),
    StraightDrawMask(0b00000000111010, InsideStraightDraw(KING, QUEEN)),
    StraightDrawMask(0b00000000110110, InsideStraightDraw(KING, JACK)),
    StraightDrawMask(0b00000000101110, InsideStraightDraw(KING, TEN)),
    StraightDrawMask(0b00000001110100, InsideStraightDraw(QUEEN, JACK)),
    StraightDrawMask(0b00000001101100, InsideStraightDraw(QUEEN, TEN)),
    StraightDrawMask(0b00000001011100, InsideStraightDraw(QUEEN, NINE)),
    StraightDrawMask(0b00000011101000, InsideStraightDraw(JACK, TEN)),
    StraightDrawMask(0b00000011011000, InsideStraightDraw(JACK, NINE)),
    StraightDrawMask(0b00000010111000, InsideStraightDraw(JACK, EIGHT)),
    StraightDrawMask(0b00000111010000, InsideStraightDraw(TEN, NINE)),
    StraightDrawMask(0b00000110110000, InsideStraightDraw(TEN, EIGHT)),
    StraightDrawMask(0b00000101110000, InsideStraightDraw(TEN, SEVEN)),
    StraightDrawMask(0b00001110100000, InsideStraightDraw(NINE, EIGHT)),
    StraightDrawMask(0b00001101100000, InsideStraightDraw(NINE, SEVEN)),
    StraightDrawMask(0b00001011100000, InsideStraightDraw(NINE, SIX)),
    StraightDrawMask(0b00011101000000, InsideStraightDraw(EIGHT, SEVEN)),
    StraightDrawMask(0b00011011000000, InsideStraightDraw(EIGHT, SIX)),
    StraightDrawMask(0b00010111000000, InsideStraightDraw(EIGHT, FIVE)),
    StraightDrawMask(0b00111010000000, InsideStraightDraw(SEVEN, SIX)),
    StraightDrawMask(0b00110110000000, InsideStraightDraw(SEVEN, FIVE)),
    StraightDrawMask(0b00101110000000, InsideStraightDraw(SEVEN, FOUR)),
    StraightDrawMask(0b01110100000000, InsideStraightDraw(SIX, FIVE)),
    StraightDrawMask(0b01101100000000, InsideStraightDraw(SIX, FOUR)),
    StraightDrawMask(0b01011100000000, InsideStraightDraw(SIX, THREE)),
    StraightDrawMask(0b11110000000000, InsideStraightDraw(FIVE, FIVE)),
    StraightDrawMask(0b11101000000000, InsideStraightDraw(FIVE, FOUR)),
    StraightDrawMask(0b11011000000000, InsideStraightDraw(FIVE, THREE)),
    StraightDrawMask(0b10111000000000, InsideStraightDraw(FIVE, TWO))
)

class DrawsEvaluatorImpl : DrawsEvaluator {
  private val handEvaluator: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluator = CardsEvaluatorBaseImpl()

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

