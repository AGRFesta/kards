package agrfesta.k.cards.texasholdem.rules.gameplay

typealias MutablePot = MutableMap<InGamePlayer, Int>
typealias Pot = Map<SeatName, Int>

fun buildMutablePot() = mutableMapOf<InGamePlayer,Int>()

fun <T: SeatName> Map<T, Int>.amount(): Int = this.values.sum()
fun <T: SeatName> Map<T, Int>.players(): Set<T> = this.keys.toSet()
fun <T: SeatName> Map<T, Int>.payedBy(player: T): Int = this[player] ?: 0

operator fun MutablePot.plus(increment: MutablePot): MutablePot =
        (this.entries.toList() + increment.entries.toList())
            .groupingBy { it.key }
            .foldTo (buildMutablePot(),0) { acc, element -> acc + element.value }

fun MutablePot.receiveFrom(player: InGamePlayer, amount: Int): Int {
    if (amount == 0) return 0
    require(amount >= 0) { "Can't receive a negative amount" }
    val effAmount = player.pay(amount)
    this[player] = this.payedBy(player) + effAmount
    return effAmount
}
private fun MutablePot.removeFrom(player: InGamePlayer, amount: Int) {
    require(amount >= 0) { "Can't remove a negative amount" }
    val newAmount = (this[player] ?: 0) - amount
    if (newAmount <= 0) {
        this.remove(player)
    } else {
        this[player] = newAmount
    }
}

fun MutablePot.extractBalancedPot(): MutablePot {
    val min: Int? = this.values.min()
    val pot = buildMutablePot()
    min?.let { m -> this.players().forEach {
        pot[it] = m
        this.removeFrom(it, m)
    } }
    return pot
}

fun MutablePot.decompose(): Collection<MutablePot> {
    val pots = mutableListOf<MutablePot>()
    while (this.isNotEmpty()) { pots += this.extractBalancedPot() }
    return pots
}


class Contribution(val player: SeatName, val amount: Int)

fun <T: SeatName> Map<T, Int>.maxContribution(): Contribution? = this.entries
        .map { Contribution(it.key,it.value) }
        .maxBy { it.amount }
