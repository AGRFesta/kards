package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.observers.ShowdownObserver
import agrfesta.k.cards.texasholdem.observers.ShowdownPlayerResult
import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.CardsEvaluator

interface Showdown {
    fun execute(pot: MutableMap<InGamePlayer,Int>, board: Board)
}

class ShowdownImpl(
        private val evaluator: CardsEvaluator,
        private val observer: ShowdownObserver?
): Showdown {
    constructor(evaluator: CardsEvaluator): this(evaluator,null)

    private val playersPrizes: MutableMap<Player,Int?> = mutableMapOf()

    override fun execute(pot: MutableMap<InGamePlayer,Int>, board: Board) {
        val playersHands = pot.players()
                .filter { !it.hasFolded() }
                .map { it to evaluator.evaluate(it.cards + board.cards()) }
                .toMap()

        pot.decompose().forEach { process(it,playersHands) }
        observer?.notifyResult(
                playersHands.entries
                        .map { ShowdownPlayerResult(it.key.player, playersPrizes[it.key.player], it.value) }
        )
    }

    private fun process(pot: MutableMap<InGamePlayer,Int>, playersHands: Map<InGamePlayer,CardsEvaluation>) {
        playersHands.entries
                .filter { pot.containsKey(it.key) }
                .groupBy( { it.value }, { it.key } )
                .entries.maxBy { it.key }
                ?.value?.let { winners ->
                    val prize = pot.amount() / winners.size
                    winners.forEach {
                        it.receive(prize)
                        playersPrizes[it.player] = playersPrizes[it.player]?:0 + prize
                    }
                }
    }

}
