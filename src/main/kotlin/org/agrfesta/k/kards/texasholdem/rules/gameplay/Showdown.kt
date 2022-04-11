package org.agrfesta.k.kards.texasholdem.rules.gameplay

import org.agrfesta.k.kards.texasholdem.observers.ShowdownObserver
import org.agrfesta.k.kards.texasholdem.observers.ShowdownPlayerResult
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluator
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Position.SMALL_BLIND

interface Showdown {
    fun execute(pot: InGamePot, board: Board, table: Table<out PlayerIdentity>)
}

class ShowdownImpl(
    private val evaluator: CardsEvaluator,
    private val observer: ShowdownObserver?
): Showdown {
    constructor(evaluator: CardsEvaluator): this(evaluator,null)

    private val playersPrizes: MutableMap<Player,UInt?> = mutableMapOf()

    override fun execute(pot: InGamePot, board: Board, table: Table<out PlayerIdentity>) {
        val playersHands = pot.players()
            .filter { !it.hasFolded() }
            .associateWith { evaluator.evaluate(it.cards + board.cards) }

        pot.decompose().forEach { process(it,playersHands,table) }
        observer?.notifyResult(
                playersHands.entries
                        .map { ShowdownPlayerResult( it.key, playersPrizes[it.key], it.value) }
        )
    }

    private fun process(pot: InGamePot,
                        playersHands: Map<InGamePlayer, CardsEvaluation>,
                        table: Table<out PlayerIdentity>) {
        playersHands.entries
            .filter { pot.containsKey(it.key) }
            .groupBy( { it.value }, { it.key } )
            .entries.maxByOrNull { it.key }
            ?.value
            ?.let { winners ->
                val spareChips = pot.amount() % winners.size.toUInt()
                val prize = (pot.amount()-spareChips) / winners.size.toUInt()
                winners.forEach {
                    it.receive(prize)
                    playersPrizes[it] = (playersPrizes[it]?:0u) + prize
                }
                assignSpareChips(spareChips, winners, table)
            }
    }

    // Eventual spare chips are assigned to the first winner at button left.
    private fun assignSpareChips(spareChips: UInt, winners: List<InGamePlayer>, table: Table<out PlayerIdentity>) {
        if (spareChips > 0u && table.players.containsAll(winners)) {
            var receiver: InGamePlayer? = null
            val iterator = table.iterateFrom(SMALL_BLIND)
            while (receiver == null) {
                val player = iterator.next()
                receiver = winners.firstOrNull { player == it }
            }
            receiver.receive(spareChips)
        }
    }

}
