package org.agrfesta.k.kards.texasholdem.rules.gameplay

class PotBuilder {
    private val pot = buildMutablePot()

    fun contribution(player: InGamePlayer, amount: UInt): PotBuilder {
        pot[player] = amount
        return this
    }

    fun build(): MutablePot = pot
}
