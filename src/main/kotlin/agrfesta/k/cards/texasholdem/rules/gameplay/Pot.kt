package agrfesta.k.cards.texasholdem.rules.gameplay

fun buildPot() = mutableMapOf<Player,Int>()

fun MutableMap<Player,Int>.amount(): Int = this.values.sum()
fun MutableMap<Player,Int>.players(): Set<Player> = this.keys.toSet()
fun MutableMap<Player,Int>.payedBy(player: Player): Int = this[player] ?: 0

operator fun MutableMap<Player,Int>.plus(increment: MutableMap<Player,Int>): MutableMap<Player,Int> =
        (this.entries.toList() + increment.entries.toList())
            .groupingBy { it.key }
            .foldTo (buildPot(),0) { acc, element -> acc + element.value }

fun MutableMap<Player,Int>.receiveFrom(player: Player, amount: Int): Int {
    if (amount < 0) throw IllegalArgumentException("Can't receive a negative amount")
    val effAmount = player.pay(amount)
    this[player] = this.payedBy(player) + effAmount
    return effAmount
}
private fun MutableMap<Player,Int>.removeFrom(player: Player, amount: Int) {
    if (amount < 0) throw IllegalArgumentException("Can't remove a negative amount")
    val newAmount = (this[player] ?: 0) - amount
    if (newAmount <= 0) {
        this.remove(player)
    } else {
        this[player] = newAmount
    }
}

fun MutableMap<Player,Int>.extractBalancedPot(): MutableMap<Player,Int> {
    val min: Int? = this.values.min()
    val pot = buildPot()
    min?.let { m -> this.players().forEach {
        pot[it] = m
        this.removeFrom(it, m)
    } }
    return pot
}

fun MutableMap<Player,Int>.decompose(): Collection<MutableMap<Player,Int>> {
    val pots = mutableListOf<MutableMap<Player,Int>>()
    while (this.isNotEmpty()) { pots += this.extractBalancedPot() }
    return pots
}


class Contribution(val player: Player, val amount: Int)

fun MutableMap<Player,Int>.maxContribution(): Contribution? = this.entries
        .map { Contribution(it.key,it.value) }
        .maxBy { it.amount }
