package org.agrfesta.k.kards.texasholdem.testing.mothers

import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.FLOP
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.PRE_FLOP
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.RIVER
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.TURN
import org.agrfesta.k.kards.texasholdem.rules.gameplay.MutablePot
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerIdentity
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.button

infix fun PlayerIdentity.payed(amount: UInt) = Pair(this, amount)

class PotPhasesBuilder private constructor() {
    private var pots: MutableMap<GamePhase, MutablePot> = mutableMapOf(
        PRE_FLOP to mutableMapOf(),
        FLOP to mutableMapOf(),
        TURN to mutableMapOf(),
        RIVER to mutableMapOf()
    )

    companion object {
        fun phasePots(setup: PotPhasesBuilder.() -> Unit): Map<GamePhase, MutablePot> {
            val builder = PotPhasesBuilder()
            builder.setup()
            return builder.build()
        }
    }

    private fun MutablePot.addPayments(vararg payments: Pair<PlayerIdentity,UInt>) {
        payments.forEach {
            this[it.first] = it.second
        }
    }

    fun atPreFlop(potAmount: UInt): PotPhasesBuilder {
        pots[PRE_FLOP]?.addPayments(button payed potAmount)
        return this
    }
    fun atPreFlop(vararg payments: Pair<PlayerIdentity,UInt>): PotPhasesBuilder {
        pots[PRE_FLOP]?.addPayments(*payments)
        return this
    }

    fun atFlop(vararg payments: Pair<PlayerIdentity,UInt>): PotPhasesBuilder {
        pots[FLOP]?.addPayments(*payments)
        return this
    }

    fun atTurn(vararg payments: Pair<PlayerIdentity,UInt>): PotPhasesBuilder {
        pots[TURN]?.addPayments(*payments)
        return this
    }

    fun atRiver(vararg payments: Pair<PlayerIdentity,UInt>): PotPhasesBuilder {
        pots[RIVER]?.addPayments(*payments)
        return this
    }

    fun build(): Map<GamePhase, MutablePot> = pots.toMap()
}

fun aPotWithValue(value: UInt): Map<GamePhase,MutablePot> = mapOf(
    PRE_FLOP to mutableMapOf(button to value),
    FLOP to mutableMapOf(),
    TURN to mutableMapOf(),
    RIVER to mutableMapOf())