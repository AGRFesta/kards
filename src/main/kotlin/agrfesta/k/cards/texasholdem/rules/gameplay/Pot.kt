package agrfesta.k.cards.texasholdem.rules.gameplay

fun buildPot() = mutableMapOf<GamePlayer,Int>()

fun MutableMap<GamePlayer,Int>.amount(): Int = this.values.sum()
fun MutableMap<GamePlayer,Int>.players(): Set<GamePlayer> = this.keys.toSet()
fun MutableMap<GamePlayer,Int>.payedBy(player: GamePlayer): Int = this[player] ?: 0

operator fun MutableMap<GamePlayer,Int>.plus(increment: MutableMap<GamePlayer,Int>): MutableMap<GamePlayer,Int> =
        (this.entries.toList() + increment.entries.toList())
            .groupingBy { it.key }
            .foldTo (buildPot(),0) { acc, element -> acc + element.value }

fun MutableMap<GamePlayer,Int>.receiveFrom(player: GamePlayer, amount: Int): Int {
    if (amount == 0) return 0
    if (amount < 0) throw IllegalArgumentException("Can't receive a negative amount")
    val effAmount = player.pay(amount)
    this[player] = this.payedBy(player) + effAmount
    return effAmount
}
private fun MutableMap<GamePlayer,Int>.removeFrom(player: GamePlayer, amount: Int) {
    if (amount < 0) throw IllegalArgumentException("Can't remove a negative amount")
    val newAmount = (this[player] ?: 0) - amount
    if (newAmount <= 0) {
        this.remove(player)
    } else {
        this[player] = newAmount
    }
}

fun MutableMap<GamePlayer,Int>.extractBalancedPot(): MutableMap<GamePlayer,Int> {
    val min: Int? = this.values.min()
    val pot = buildPot()
    min?.let { m -> this.players().forEach {
        pot[it] = m
        this.removeFrom(it, m)
    } }
    return pot
}

fun MutableMap<GamePlayer,Int>.decompose(): Collection<MutableMap<GamePlayer,Int>> {
    val pots = mutableListOf<MutableMap<GamePlayer,Int>>()
    while (this.isNotEmpty()) { pots += this.extractBalancedPot() }
    return pots
}


class Contribution(val player: GamePlayer, val amount: Int)

fun MutableMap<GamePlayer,Int>.maxContribution(): Contribution? = this.entries
        .map { Contribution(it.key,it.value) }
        .maxBy { it.amount }
