package agrfesta.k.cards.texasholdem.rules.gameplay

typealias Pot = MutableMap<InGamePlayer, Int>

fun buildPot() = mutableMapOf<InGamePlayer,Int>()

fun Pot.amount(): Int = this.values.sum()
fun Pot.players(): Set<InGamePlayer> = this.keys.toSet()
fun Pot.payedBy(player: InGamePlayer): Int = this[player] ?: 0

operator fun Pot.plus(increment: Pot): Pot =
        (this.entries.toList() + increment.entries.toList())
            .groupingBy { it.key }
            .foldTo (buildPot(),0) { acc, element -> acc + element.value }

fun Pot.receiveFrom(player: InGamePlayer, amount: Int): Int {
    if (amount == 0) return 0
    require(amount >= 0) { "Can't receive a negative amount" }
    val effAmount = player.pay(amount)
    this[player] = this.payedBy(player) + effAmount
    return effAmount
}
private fun Pot.removeFrom(player: InGamePlayer, amount: Int) {
    require(amount >= 0) { "Can't remove a negative amount" }
    val newAmount = (this[player] ?: 0) - amount
    if (newAmount <= 0) {
        this.remove(player)
    } else {
        this[player] = newAmount
    }
}

fun Pot.extractBalancedPot(): Pot {
    val min: Int? = this.values.min()
    val pot = buildPot()
    min?.let { m -> this.players().forEach {
        pot[it] = m
        this.removeFrom(it, m)
    } }
    return pot
}

fun Pot.decompose(): Collection<Pot> {
    val pots = mutableListOf<Pot>()
    while (this.isNotEmpty()) { pots += this.extractBalancedPot() }
    return pots
}


class Contribution(val player: InGamePlayer, val amount: Int)

fun Pot.maxContribution(): Contribution? = this.entries
        .map { Contribution(it.key,it.value) }
        .maxBy { it.amount }
