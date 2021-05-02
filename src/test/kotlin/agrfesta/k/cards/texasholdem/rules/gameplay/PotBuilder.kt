package agrfesta.k.cards.texasholdem.rules.gameplay

class PotBuilder {
    private val pot = buildMutablePot()

    fun contribution(player: InGamePlayer, amount: Int): PotBuilder {
        pot[player] = amount
        return this
    }

    fun build(): MutablePot = pot

}
