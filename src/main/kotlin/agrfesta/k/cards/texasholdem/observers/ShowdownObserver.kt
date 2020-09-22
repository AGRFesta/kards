package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.gameplay.Player

interface ShowdownObserver {
    fun notifyResult(result: Collection<ShowdownPlayerResult>)
}

//class ShowdownMultiObserver(): ShowdownObserver {
//    private val observers: Collection<ShowdownObserver> = listOf()
//
//    override fun notifyResult(result: Collection<ShowdownPlayerResult>) {
//        observers.forEach { it.notifyResult(result) }
//    }
//
//    fun subscribe(observer: ShowdownObserver) {
//        observers + observer
//    }
//}

class ShowdownPlayerResult(val player: Player, val prize: Int?, val evaluation: CardsEvaluation)
