package org.agrfesta.k.kards.texasholdem.rules.gameplay

typealias Pot<P> = Map<P, UInt>
typealias MutablePot<P> = MutableMap<P, UInt>
typealias InGamePot = MutablePot<InGamePlayer>

fun buildMutablePot() = mutableMapOf<InGamePlayer,UInt>()

fun <T: SeatName> Pot<T>.amount(): UInt = values.sum()
fun <T: SeatName> Pot<T>.players(): Set<T> = keys.toSet()
fun <T: SeatName> Pot<T>.payedBy(player: T): UInt = this[player] ?: 0u

operator fun <T: SeatName> Pot<T>.plus(increment: Pot<T>): Pot<T> =
    (entries.toList() + increment.entries.toList())
        .groupingBy { it.key }
        .foldTo(mutableMapOf(), 0u) { acc, element -> acc + element.value }

fun InGamePot.receiveFrom(player: InGamePlayer, amount: UInt): UInt {
    if (amount == 0u) return 0u
    val effAmount = player.pay(amount)
    this[player] = payedBy(player) + effAmount
    return effAmount
}
private fun InGamePot.removeFrom(player: InGamePlayer, amount: UInt) {
    val newAmount = (this[player] ?: 0u) - amount
    if (newAmount == 0u) {
        remove(player)
    } else {
        this[player] = newAmount
    }
}

fun InGamePot.extractBalancedPot(): InGamePot {
    val min: UInt? = values.minOrNull()
    val pot = buildMutablePot()
    min?.let { m -> players().forEach {
        pot[it] = m
        removeFrom(it, m)
    } }
    return pot
}

fun InGamePot.decompose(): Collection<InGamePot> {
    val pots = mutableListOf<InGamePot>()
    while (isNotEmpty()) { pots += extractBalancedPot() }
    return pots
}

class Contribution(val player: SeatName, val amount: UInt)

fun <T: SeatName> Pot<T>.maxContribution(): Contribution? = entries
        .map { Contribution(it.key,it.value) }
        .maxByOrNull { it.amount }
