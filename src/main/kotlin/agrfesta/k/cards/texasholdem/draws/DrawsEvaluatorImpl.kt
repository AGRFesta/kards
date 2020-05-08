package agrfesta.k.cards.texasholdem.draws

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.CardsEvaluator
import agrfesta.k.cards.texasholdem.rules.CardsEvaluatorBaseImpl
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand
import agrfesta.kcards.playingcards.cards.Card
import agrfesta.kcards.playingcards.cards.Seed
import agrfesta.kcards.playingcards.suits.*
import java.util.Comparator
import java.util.HashSet

class DrawsEvaluatorImpl: DrawsEvaluator {
    private val handEvaluator: CardsEvaluator = CardsEvaluatorBaseImpl()

    private val masks = listOf(
            StraightDrawMask(187, DoubleInsideStraightDraw(JACK, ACE)),     //00000010111011
            StraightDrawMask(374, DoubleInsideStraightDraw(TEN, KING)),     //00000101110110
            StraightDrawMask(748, DoubleInsideStraightDraw(NINE, QUEEN)),   //00001011101100
            StraightDrawMask(1496, DoubleInsideStraightDraw(EIGHT, JACK)),  //00010111011000
            StraightDrawMask(2992, DoubleInsideStraightDraw(SEVEN, TEN)),   //00101110110000
            StraightDrawMask(5984, DoubleInsideStraightDraw(SIX, NINE)),    //01011101100000
            StraightDrawMask(11968, DoubleInsideStraightDraw(FIVE, EIGHT)), //10111011000000

            StraightDrawMask(93, DoubleInsideStraightDraw(QUEEN, ACE)),     //00000001011101
            StraightDrawMask(186, DoubleInsideStraightDraw(JACK, KING)),    //00000010111010
            StraightDrawMask(372, DoubleInsideStraightDraw(TEN, QUEEN)),    //00000101110100
            StraightDrawMask(744, DoubleInsideStraightDraw(NINE, JACK)),    //00001011101000
            StraightDrawMask(1488, DoubleInsideStraightDraw(EIGHT, TEN)),   //00010111010000
            StraightDrawMask(2976, DoubleInsideStraightDraw(SEVEN, NINE)),  //00101110100000
            StraightDrawMask(5952, DoubleInsideStraightDraw(SIX, EIGHT)),   //01011101000000
            StraightDrawMask(11904, DoubleInsideStraightDraw(FIVE, SEVEN)), //10111010000000

            StraightDrawMask(30, OutsideStraightDraw(KING)),                //00000000011110
            StraightDrawMask(60, OutsideStraightDraw(QUEEN)),               //00000000111100
            StraightDrawMask(120, OutsideStraightDraw(JACK)),               //00000001111000
            StraightDrawMask(240, OutsideStraightDraw(TEN)),                //00000011110000
            StraightDrawMask(480, OutsideStraightDraw(NINE)),               //00000111100000
            StraightDrawMask(960, OutsideStraightDraw(EIGHT)),              //00001111000000
            StraightDrawMask(1920, OutsideStraightDraw(SEVEN)),             //00011110000000
            StraightDrawMask(3840, OutsideStraightDraw(SIX)),               //00111100000000
            StraightDrawMask(7680, OutsideStraightDraw(FIVE)),              //01111000000000

            StraightDrawMask(29, InsideStraightDraw(ACE, KING)),            //00000000011101
            StraightDrawMask(27, InsideStraightDraw(ACE, QUEEN)),           //00000000011011
            StraightDrawMask(23, InsideStraightDraw(ACE, JACK)),            //00000000010111
            StraightDrawMask(15, InsideStraightDraw(ACE, TEN)),             //00000000001111
            StraightDrawMask(58, InsideStraightDraw(KING, QUEEN)),          //00000000111010
            StraightDrawMask(54, InsideStraightDraw(KING, JACK)),           //00000000110110
            StraightDrawMask(46, InsideStraightDraw(KING, TEN)),            //00000000101110
            StraightDrawMask(116, InsideStraightDraw(QUEEN, JACK)),         //00000001110100
            StraightDrawMask(108, InsideStraightDraw(QUEEN, TEN)),          //00000001101100
            StraightDrawMask(92, InsideStraightDraw(QUEEN, NINE)),          //00000001011100
            StraightDrawMask(232, InsideStraightDraw(JACK, TEN)),           //00000011101000
            StraightDrawMask(216, InsideStraightDraw(JACK, NINE)),          //00000011011000
            StraightDrawMask(184, InsideStraightDraw(JACK, EIGHT)),         //00000010111000
            StraightDrawMask(464, InsideStraightDraw(TEN, NINE)),           //00000111010000
            StraightDrawMask(432, InsideStraightDraw(TEN, EIGHT)),          //00000110110000
            StraightDrawMask(368, InsideStraightDraw(TEN, SEVEN)),          //00000101110000
            StraightDrawMask(928, InsideStraightDraw(NINE, EIGHT)),         //00001110100000
            StraightDrawMask(864, InsideStraightDraw(NINE, SEVEN)),         //00001101100000
            StraightDrawMask(736, InsideStraightDraw(NINE, SIX)),           //00001011100000
            StraightDrawMask(1856, InsideStraightDraw(EIGHT, SEVEN)),       //00011101000000
            StraightDrawMask(1728, InsideStraightDraw(EIGHT, SIX)),         //00011011000000
            StraightDrawMask(1472, InsideStraightDraw(EIGHT, FIVE)),        //00010111000000
            StraightDrawMask(3712, InsideStraightDraw(SEVEN, SIX)),         //00111010000000
            StraightDrawMask(3456, InsideStraightDraw(SEVEN, FIVE)),        //00110110000000
            StraightDrawMask(2944, InsideStraightDraw(SEVEN, FOUR)),        //00101110000000
            StraightDrawMask(7424, InsideStraightDraw(SIX, FIVE)),          //01110100000000
            StraightDrawMask(6912, InsideStraightDraw(SIX, FOUR)),          //01101100000000
            StraightDrawMask(5888, InsideStraightDraw(SIX, THREE)),         //01011100000000
            StraightDrawMask(15360, InsideStraightDraw(FIVE, FIVE)),        //11110000000000
            StraightDrawMask(14848, InsideStraightDraw(FIVE, FOUR)),        //11101000000000
            StraightDrawMask(13824, InsideStraightDraw(FIVE, THREE)),       //11011000000000
            StraightDrawMask(11776, InsideStraightDraw(FIVE, TWO))          //10111000000000

    )

    //TODO reuse refactoring from CardsEvaluatorBaseImpl
    private val cardComparator: Comparator<Card> = compareBy(Card::rank)
            .thenBy(compareBy(Seed::ord), Card::seed)

    override fun evaluate(set: Set<Card>, ce: CardsEvaluation?): Set<Draw> {
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

    //TODO reuse refactoring from CardsEvaluatorBaseImpl
    private fun buildHistogram(hand: Collection<Card>): List<Set<Seed>> {
        val histogram = (0 .. 12).map { HashSet<Seed>() }
        hand.forEach { histogram[it.rank().ordinal()].add(it.seed()) }
        return histogram
    }

    //TODO reuse refactoring from CardsEvaluatorBaseImpl
    private fun getBitSequence(
            histogram: List<Set<Seed>>,
            predicate: (set: Set<Seed>) -> Boolean): Int {
        return histogram.indices
                .map { if (predicate(histogram[it])) 1 shl it else 0 }
                .sum() + if (predicate(histogram[0])) 1 shl histogram.size else 0
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

    //TODO reuse refactoring from CardsEvaluatorBaseImpl
    private fun groupBySeed(set: Set<Card>): Map<Seed, List<Card>> = set
            .groupingBy { it.seed() }
            .fold(  { _: Seed, _: Card -> listOf()},
                    { _, accumulator, element ->
                        accumulator.plus(element)
                    })

    private fun findFlushDraw(groupBySeed: Map<Seed, List<Card>>): Draw? {
        return groupBySeed.entries
                .filter { it.value.size == 4 }
                .map { createFlushDraw(it.key, it.value) }
                .firstOrNull()
    }
    private fun createFlushDraw(seed: Seed, cards: Collection<Card>): FlushDraw {
        val ranks = cards.take(4).map { it.rank() }
        return FlushDraw(ranks[0],ranks[1],ranks[2],ranks[3],seed)
    }
}

private data class StraightDrawMask(val value: Int, val draw: Draw)

