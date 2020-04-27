package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.rules.CardsEvaluator

interface Showdown {
    fun execute(pot: MutableMap<Player,Int>, board: Board)
}

class ShowdownImpl(private val evaluator: CardsEvaluator): Showdown {

    override fun execute(pot: MutableMap<Player,Int>, board: Board) {
        pot.decompose().forEach { process(it,board) }
    }

    private fun process(pot: MutableMap<Player,Int>, board: Board) {
        pot.players()
            .filter { !it.hasFolded() }
            .groupBy { evaluator.evaluate(it.cards + board.cards()) }
            .entries.maxBy { it.key }
            ?.value?.let { w ->
                val prize = pot.amount() / w.size
                w.forEach { it.receive(prize) }
            }
    }

}