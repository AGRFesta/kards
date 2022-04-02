package org.agrfesta.k.kards.texasholdem.rules.gameplay

import org.agrfesta.k.kards.texasholdem.observers.ShowdownObserver
import org.agrfesta.k.kards.texasholdem.observers.ShowdownPlayerResult
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation

interface Showdown {
    fun execute(pot: InGamePot, board: Board)
}

class ShowdownImpl(
    private val evaluator: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluator,
    private val observer: ShowdownObserver?
): Showdown {
    constructor(evaluator: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluator): this(evaluator,null)

    private val playersPrizes: MutableMap<Player,UInt?> = mutableMapOf()

    override fun execute(pot: InGamePot, board: Board) {
        val playersHands = pot.players()
                .filter { !it.hasFolded() }
                .map { it to evaluator.evaluate(it.cards + board.cards) }
                .toMap()

        pot.decompose().forEach { process(it,playersHands) }
        observer?.notifyResult(
                playersHands.entries
                        .map { ShowdownPlayerResult( it.key, playersPrizes[it.key], it.value) }
        )
    }

    private fun process(pot: InGamePot, playersHands: Map<InGamePlayer, CardsEvaluation>) {
        playersHands.entries
                .filter { pot.containsKey(it.key) }
                .groupBy( { it.value }, { it.key } )
                .entries.maxByOrNull { it.key }
                ?.value?.let { winners ->
                    val prize = pot.amount() / winners.size.toUInt()
                    winners.forEach {
                        it.receive(prize)
                        playersPrizes[it] = (playersPrizes[it]?:0u) + prize
                    }
                }
    }

}
