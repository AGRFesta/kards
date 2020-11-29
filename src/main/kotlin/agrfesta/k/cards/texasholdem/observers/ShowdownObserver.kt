package agrfesta.k.cards.texasholdem.observers

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStack

interface ShowdownObserver {
    fun notifyResult(result: Collection<ShowdownPlayerResult>) {}
}

class ShowdownPlayerResult(val player: PlayerStack, val prize: Int?, val evaluation: CardsEvaluation)
