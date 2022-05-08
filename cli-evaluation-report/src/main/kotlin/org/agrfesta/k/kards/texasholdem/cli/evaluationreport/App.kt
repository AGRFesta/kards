package org.agrfesta.k.kards.texasholdem.cli.evaluationreport

import com.github.ajalt.mordant.TermColors
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluator
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluatorBaseImpl
import org.agrfesta.k.kards.texasholdem.rules.hands.THPokerHand
import org.agrfesta.k.kards.texasholdem.rules.hands.THPokerHand.FLUSH
import org.agrfesta.k.kards.texasholdem.rules.hands.THPokerHand.FOUR_OF_A_KIND
import org.agrfesta.k.kards.texasholdem.rules.hands.THPokerHand.FULL_HOUSE
import org.agrfesta.k.kards.texasholdem.rules.hands.THPokerHand.HIGH_CARD
import org.agrfesta.k.kards.texasholdem.rules.hands.THPokerHand.PAIR
import org.agrfesta.k.kards.texasholdem.rules.hands.THPokerHand.STRAIGHT
import org.agrfesta.k.kards.texasholdem.rules.hands.THPokerHand.STRAIGHT_FLUSH
import org.agrfesta.k.kards.texasholdem.rules.hands.THPokerHand.THREE_OF_A_KIND
import org.agrfesta.k.kards.texasholdem.rules.hands.THPokerHand.TWO_PAIR
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

object App {
    const val appName = "DBS CLI App"
    const val version = "0.1.0"
}

fun main(args: Array<String>) {
    val parser = ArgParser("${App.appName}:: ${App.version}")
    val version by parser.option(ArgType.Boolean, shortName = "V", description = "Version").default(false)

    // Add all input to parser
    parser.parse(args)

    if(version) println(App.version)

    val evaluator: CardsEvaluator = CardsEvaluatorBaseImpl()
    test5(evaluator)
    println("-------------------------------------")
    test6(evaluator)
    println("-------------------------------------")
    test7(evaluator)
}

fun test5(evaluator: CardsEvaluator) {
    mainMessage("Evaluating all five cards hands...")
    val countMap = countEvaluations(evaluator, 5)
    countMap.checkTHPokerHandFrequency(STRAIGHT_FLUSH, 40)
    countMap.checkTHPokerHandFrequency(FOUR_OF_A_KIND, 624)
    countMap.checkTHPokerHandFrequency(FULL_HOUSE, 3_744)
    countMap.checkTHPokerHandFrequency(FLUSH, 5_108)
    countMap.checkTHPokerHandFrequency(STRAIGHT, 10_200)
    countMap.checkTHPokerHandFrequency(THREE_OF_A_KIND, 54_912)
    countMap.checkTHPokerHandFrequency(TWO_PAIR, 123_552)
    countMap.checkTHPokerHandFrequency(PAIR, 1_098_240)
    countMap.checkTHPokerHandFrequency(HIGH_CARD, 1_302_540)
}

fun test6(evaluator: CardsEvaluator) {
    mainMessage("Evaluating all six cards hands...")
    val countMap = countEvaluations(evaluator, 6)
    countMap.checkTHPokerHandFrequency(STRAIGHT_FLUSH, 1_844)
    countMap.checkTHPokerHandFrequency(FOUR_OF_A_KIND, 14_664)
    countMap.checkTHPokerHandFrequency(FULL_HOUSE, 165_984)
    countMap.checkTHPokerHandFrequency(FLUSH, 205_792)
    countMap.checkTHPokerHandFrequency(STRAIGHT, 361_620)
    countMap.checkTHPokerHandFrequency(THREE_OF_A_KIND, 732_160)
    countMap.checkTHPokerHandFrequency(TWO_PAIR, 2_532_816)
    countMap.checkTHPokerHandFrequency(PAIR, 9_730_740)
    countMap.checkTHPokerHandFrequency(HIGH_CARD, 6_612_900)
}

fun test7(evaluator: CardsEvaluator) {
    mainMessage("Evaluating all seven cards hands...")
    val countMap = countEvaluations(evaluator, 7)
    countMap.checkTHPokerHandFrequency(STRAIGHT_FLUSH, 41_584)
    countMap.checkTHPokerHandFrequency(FOUR_OF_A_KIND, 224_848)
    countMap.checkTHPokerHandFrequency(FULL_HOUSE, 3_473_184)
    countMap.checkTHPokerHandFrequency(FLUSH, 4_047_644)
    countMap.checkTHPokerHandFrequency(STRAIGHT, 6_180_020)
    countMap.checkTHPokerHandFrequency(THREE_OF_A_KIND, 6_461_620)
    countMap.checkTHPokerHandFrequency(TWO_PAIR, 31_433_400)
    countMap.checkTHPokerHandFrequency(PAIR, 58_627_800)
    countMap.checkTHPokerHandFrequency(HIGH_CARD, 23_294_460)
}

@OptIn(ExperimentalTime::class)
private fun countEvaluations(evaluator: CardsEvaluator, handSize: Int): Map<THPokerHand,Long> {
    val generator = AllPossibleHandsIterator(handSize)
    val countMap = mutableMapOf<THPokerHand,Long>()
    var elapsedCount = 0L
    generator.asSequence()
        .forEach {
            val (value, time) = measureTimedValue { evaluator.evaluate(it) }
            elapsedCount += time.inWholeNanoseconds
            countMap.merge(value.getHandValue(), 1, Long::plus)
        }
    val tot: Long = countMap.values.sum()
    val average = elapsedCount / tot
    with(TermColors(TermColors.Level.TRUECOLOR)) {
        println("Hands evaluated: ${blue("$tot")}")
        println("Six cards hand average evaluation: ${blue("$average")} nanoseconds")
    }
    return countMap
}

private fun Map<THPokerHand,Long>.checkTHPokerHandFrequency(hand: THPokerHand, expected: Long) {
    with(TermColors(TermColors.Level.TRUECOLOR)) {
        val map = this@checkTHPokerHandFrequency
        println("$hand frequency: ${map[hand]} ${check(map[hand], expected)}")
    }
}
private fun TermColors.check(actual: Long?, expected: Long) =
    if (actual == expected) green("OK!") else red("FAILED! should be $expected")

private fun mainMessage(msg: String) {
    with(TermColors(TermColors.Level.TRUECOLOR)) {
        println(yellow(msg))
    }
}