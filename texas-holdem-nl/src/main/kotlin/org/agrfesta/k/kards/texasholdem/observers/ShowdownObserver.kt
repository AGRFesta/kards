package org.agrfesta.k.kards.texasholdem.observers

import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation
import org.agrfesta.k.kards.texasholdem.rules.gameplay.InGamePlayer

interface ShowdownObserver {
    fun notifyResult(result: Collection<ShowdownPlayerResult>) {}
}

class ShowdownPlayerResult(val player: InGamePlayer, val prize: UInt?, val evaluation: CardsEvaluation)
