package agrfesta.k.cards.texasholdem.rules.gameplay

class PotBuilder {
    private val pot = buildPot()

    fun contribution(player: Player, amount: Int): PotBuilder {
        pot[player] = amount
        return this
    }

    fun build(): MutableMap<Player,Int> = pot

}