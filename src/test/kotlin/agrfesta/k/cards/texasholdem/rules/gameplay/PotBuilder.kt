package agrfesta.k.cards.texasholdem.rules.gameplay

class PotBuilder {
    private val pot = buildPot()

    fun contribution(player: InGamePlayer, amount: Int): PotBuilder {
        pot[player] = amount
        return this
    }

    fun build(): Pot = pot

}
