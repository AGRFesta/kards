package agrfesta.k.cards.texasholdem.rules.gameplay

typealias Pot<P> = Map<P, Int>
typealias MutablePot<P> = MutableMap<P, Int>
typealias InGamePot = MutablePot<InGamePlayer>

fun buildMutablePot() = mutableMapOf<InGamePlayer,Int>()

fun <T: SeatName> Pot<T>.amount(): Int = this.values.sum()
fun <T: SeatName> Pot<T>.players(): Set<T> = this.keys.toSet()
fun <T: SeatName> Pot<T>.payedBy(player: T): Int = this[player] ?: 0

operator fun <T: SeatName> Pot<T>.plus(increment: Pot<T>): Pot<T> =
    (this.entries.toList() + increment.entries.toList())
        .groupingBy { it.key }
        .foldTo(mutableMapOf(), 0, { acc, element -> acc + element.value })

fun InGamePot.receiveFrom(player: InGamePlayer, amount: Int): Int {
    if (amount == 0) return 0
    require(amount >= 0) { "Can't receive a negative amount" }
    val effAmount = player.pay(amount)
    this[player] = this.payedBy(player) + effAmount
    return effAmount
}
private fun InGamePot.removeFrom(player: InGamePlayer, amount: Int) {
    require(amount >= 0) { "Can't remove a negative amount" }
    val newAmount = (this[player] ?: 0) - amount
    if (newAmount <= 0) {
        this.remove(player)
    } else {
        this[player] = newAmount
    }
}

fun InGamePot.extractBalancedPot(): InGamePot {
    val min: Int? = this.values.min()
    val pot = buildMutablePot()
    min?.let { m -> this.players().forEach {
        pot[it] = m
        this.removeFrom(it, m)
    } }
    return pot
}

fun InGamePot.decompose(): Collection<InGamePot> {
    val pots = mutableListOf<InGamePot>()
    while (this.isNotEmpty()) { pots += this.extractBalancedPot() }
    return pots
}

class Contribution(val player: SeatName, val amount: Int)

fun <T: SeatName> Pot<T>.maxContribution(): Contribution? = this.entries
        .map { Contribution(it.key,it.value) }
        .maxBy { it.amount }
