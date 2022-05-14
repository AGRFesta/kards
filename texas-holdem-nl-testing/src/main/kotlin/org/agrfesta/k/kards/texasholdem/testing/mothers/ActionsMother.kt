package org.agrfesta.k.kards.texasholdem.testing.mothers

import org.agrfesta.k.kards.texasholdem.rules.gameplay.Action
import org.agrfesta.k.kards.texasholdem.rules.gameplay.ActionImpl
import org.agrfesta.k.kards.texasholdem.rules.gameplay.ActionType
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.*
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerAction

fun anAction(type: ActionType = ActionType.Fold, amount: UInt? = null): Action = ActionImpl(type, amount)
val anAction = anAction()

class ActionsHistoryBuilder private constructor() {
    private var history: MutableMap<GamePhase, List<PlayerAction>> = mutableMapOf(
        PRE_FLOP to emptyList(),
        FLOP to emptyList(),
        TURN to emptyList(),
        RIVER to emptyList()
    )

    companion object {
        fun history(setup: ActionsHistoryBuilder.() -> Unit): Map<GamePhase, List<PlayerAction>> {
            val builder = ActionsHistoryBuilder()
            builder.setup()
            return builder.build()
        }
    }

    fun atPreFlop(vararg history: PlayerAction): ActionsHistoryBuilder {
        this.history[PRE_FLOP] = history.toList()
        return this
    }

    fun atFlop(vararg history: PlayerAction): ActionsHistoryBuilder {
        this.history[FLOP] = history.toList()
        return this
    }

    fun atTurn(vararg history: PlayerAction): ActionsHistoryBuilder {
        this.history[TURN] = history.toList()
        return this
    }

    fun atRiver(vararg history: PlayerAction): ActionsHistoryBuilder {
        this.history[RIVER] = history.toList()
        return this
    }

    fun build(): Map<GamePhase, List<PlayerAction>> = history.toMap()
}
