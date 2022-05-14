package org.agrfesta.k.kards.texasholdem.rules.gameplay

typealias Pot = Map<PlayerIdentity, UInt>
typealias MutablePot = MutableMap<PlayerIdentity, UInt>

fun buildMutablePot() = mutableMapOf<PlayerIdentity,UInt>()

fun Pot.amount(): UInt = values.sum()
fun Pot.players(): Set<PlayerIdentity> = keys.toSet()
fun Pot.payedBy(player: PlayerIdentity): UInt = this[player] ?: 0u
fun Pot.toMutablePot(): MutablePot = this.toMutableMap()

operator fun MutablePot.plus(increment: MutablePot): MutablePot =
    (entries.toList() + increment.entries.toList())
        .groupingBy { it.key }
        .foldTo(mutableMapOf(), 0u) { acc, element -> acc + element.value }

fun MutablePot.receiveFrom(player: InGamePlayer, amount: UInt): UInt {
    if (amount == 0u) return 0u
    val effAmount = player.pay(amount)
    this[player] = payedBy(player) + effAmount
    return effAmount
}

private fun MutablePot.removeFrom(player: PlayerIdentity, amount: UInt) {
    val newAmount = (this[player] ?: 0u) - amount
    if (newAmount == 0u) {
        remove(player)
    } else {
        this[player] = newAmount
    }
}

fun MutablePot.extractBalancedPot(): MutablePot {
    val min: UInt? = values.minOrNull()
    val pot = buildMutablePot()
    min?.let { m -> players().forEach {
        pot[it] = m
        removeFrom(it, m)
    } }
    return pot
}

fun MutablePot.decompose(): Collection<MutablePot> {
    val pots = mutableListOf<MutablePot>()
    while (isNotEmpty()) { pots += extractBalancedPot() }
    return pots
}

class Contribution(val player: PlayerIdentity, val amount: UInt)

fun Pot.maxContribution(): Contribution? = entries
    .map { Contribution(it.key,it.value) }
    .maxByOrNull { it.amount }
