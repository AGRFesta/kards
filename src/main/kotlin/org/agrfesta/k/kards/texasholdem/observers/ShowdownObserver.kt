package org.agrfesta.k.kards.texasholdem.observers

import org.agrfesta.k.kards.texasholdem.rules.gameplay.InGamePlayer

interface ShowdownObserver {
    fun notifyResult(result: Collection<ShowdownPlayerResult>) {}
}

class ShowdownPlayerResult(val player: InGamePlayer, val prize: Int?, val evaluation: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation)

fun multipleShowdownObserverOf(vararg observers: ShowdownObserver?) =
    multipleShowdownObserverOf(observers
        .filterNotNull()
        .toSet())

fun multipleShowdownObserverOf(observers: Set<ShowdownObserver>) = object: ShowdownObserver {
    override fun notifyResult(result: Collection<ShowdownPlayerResult>) {
        observers.forEach { it.notifyResult(result) }
    }
}
