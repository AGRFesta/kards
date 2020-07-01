package agrfesta.k.cards.texasholdem.rules

import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand
import agrfesta.k.cards.playingcards.suits.createFrenchDeck

/*  Hand	        Frequency	Probability	Cumulative	Odds (odds against)

    Royal flush	    4,324	    0.0032%	    0.0032%	    30,939 : 1
    Straight flush 	37,260	    0.0279%	    0.0311%	    3,589.6 : 1
    Four of a kind	224,848	    0.168%	    0.199%	    594 : 1
    Full house	    3,473,184	2.60%	    2.80%	    37.5 : 1
    Flush	        4,047,644	3.03%	    5.82%	    32.1 : 1
    Straight	    6,180,020	4.62%	    10.4%	    20.6 : 1
    Three of a kind	6,461,620	4.83%	    15.3%	    19.7 : 1
    Two pair	    31,433,400	23.5%	    38.8%	    3.26 : 1
    One pair	    58,627,800	43.8%	    82.6%	    1.28 : 1
    No pair	        23,294,460	17.4%	    100%	    4.74 : 1

    Total	        133,784,560	100%	    100%	    0 : 1

    https://en.wikipedia.org/wiki/Poker_probability */

val evaluator = CardsEvaluatorBaseImpl()
const val num = 100_000_000

fun main() {
    val start = System.currentTimeMillis()
    val result = (0 .. num)
            .map { play() }
            .groupingBy { it }
            .eachCount()
    val time = System.currentTimeMillis() - start
    printResult(result)
    println("ExecTime: ${time/1000} seconds")
    println("ExecTime: ${num/time} E/ms")
}

fun play(): THPokerHand {
    return evaluator.evaluate(
            createFrenchDeck().draw(7).toSet()
        ).getHandValue()
}

fun printResult(result: Map<THPokerHand,Int>) {
    val res = result.entries
            .sortedByDescending { it.key }
            .map { "[${it.key}] prob: ${it.value*100.0/num}%\n" }
            .reduce { acc, string -> acc + string  }
    println(res)
}