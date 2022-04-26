package org.agrfesta.k.kards.texasholdem.testing.mothers

import org.agrfesta.k.cards.playingcards.utils.circularIterator
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Action
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GameContext
import org.agrfesta.k.kards.texasholdem.rules.gameplay.OwnPlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStrategyInterface
import org.agrfesta.k.kards.texasholdem.rules.gameplay.call
import org.agrfesta.k.kards.texasholdem.rules.gameplay.fold
import org.agrfesta.k.kards.texasholdem.testing.mothers.CircularStrategy.Companion.aStrategy

class CircularStrategy private constructor(list: List<Action>): PlayerStrategyInterface {
    private val iterator = list.circularIterator()

    companion object {
        fun aStrategy(vararg actions: Action = arrayOf(anAction)): PlayerStrategyInterface =
            CircularStrategy(actions.toList())
    }

    override fun invoke(p1: OwnPlayer, p2: GameContext): Action = iterator.next()
}

val folder = aStrategy(fold())
val limper = aStrategy(call())
