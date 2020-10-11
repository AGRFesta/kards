package agrfesta.k.cards.texasholdem.rules.gameplay

fun buildPot() = mutableMapOf<InGamePlayer,Int>()

fun MutableMap<InGamePlayer,Int>.amount(): Int = this.values.sum()
fun MutableMap<InGamePlayer,Int>.players(): Set<InGamePlayer> = this.keys.toSet()
fun MutableMap<InGamePlayer,Int>.payedBy(player: InGamePlayer): Int = this[player] ?: 0

operator fun MutableMap<InGamePlayer,Int>.plus(increment: MutableMap<InGamePlayer,Int>): MutableMap<InGamePlayer,Int> =
        (this.entries.toList() + increment.entries.toList())
            .groupingBy { it.key }
            .foldTo (buildPot(),0) { acc, element -> acc + element.value }

fun MutableMap<InGamePlayer,Int>.receiveFrom(player: InGamePlayer, amount: Int): Int {
    if (amount == 0) return 0
    require(amount >= 0) { "Can't receive a negative amount" }
    val effAmount = player.pay(amount)
    this[player] = this.payedBy(player) + effAmount
    return effAmount
}
private fun MutableMap<InGamePlayer,Int>.removeFrom(player: InGamePlayer, amount: Int) {
    require(amount >= 0) { "Can't remove a negative amount" }
    val newAmount = (this[player] ?: 0) - amount
    if (newAmount <= 0) {
        this.remove(player)
    } else {
        this[player] = newAmount
    }
}

fun MutableMap<InGamePlayer,Int>.extractBalancedPot(): MutableMap<InGamePlayer,Int> {
    val min: Int? = this.values.min()
    val pot = buildPot()
    min?.let { m -> this.players().forEach {
        pot[it] = m
        this.removeFrom(it, m)
    } }
    return pot
}

fun MutableMap<InGamePlayer,Int>.decompose(): Collection<MutableMap<InGamePlayer,Int>> {
    val pots = mutableListOf<MutableMap<InGamePlayer,Int>>()
    while (this.isNotEmpty()) { pots += this.extractBalancedPot() }
    return pots
}


class Contribution(val player: InGamePlayer, val amount: Int)

fun MutableMap<InGamePlayer,Int>.maxContribution(): Contribution? = this.entries
        .map { Contribution(it.key,it.value) }
        .maxBy { it.amount }
