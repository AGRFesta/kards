package org.agrfesta.k.kards.texasholdem.rules.gameplay

import org.agrfesta.k.kards.texasholdem.observers.ShowdownObserver
import org.agrfesta.k.kards.texasholdem.observers.ShowdownPlayerResult

interface Showdown {
    fun execute(pot: InGamePot, board: Board)
}

class ShowdownImpl(
    private val evaluator: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluator,
    private val observer: ShowdownObserver?
): Showdown {
    constructor(evaluator: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluator): this(evaluator,null)

    private val playersPrizes: MutableMap<Player,Int?> = mutableMapOf()

    override fun execute(pot: InGamePot, board: Board) {
        val playersHands = pot.players()
                .filter { !it.hasFolded() }
                .map { it to evaluator.evaluate(it.cards + board.cards) }
                .toMap()

        pot.decompose().forEach { process(it,playersHands) }
        observer?.notifyResult(
                playersHands.entries
                        .map { ShowdownPlayerResult( it.key, playersPrizes[it.key.player], it.value) }
        )
    }

    private fun process(pot: InGamePot, playersHands: Map<InGamePlayer, org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation>) {
        playersHands.entries
                .filter { pot.containsKey(it.key) }
                .groupBy( { it.value }, { it.key } )
                .entries.maxByOrNull { it.key }
                ?.value?.let { winners ->
                    val prize = pot.amount() / winners.size
                    winners.forEach {
                        it.receive(prize)
                        playersPrizes[it.player] = (playersPrizes[it.player]?:0) + prize
                    }
                }
    }

}
