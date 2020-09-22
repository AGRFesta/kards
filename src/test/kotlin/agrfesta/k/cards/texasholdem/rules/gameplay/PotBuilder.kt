package agrfesta.k.cards.texasholdem.rules.gameplay

class PotBuilder {
    private val pot = buildPot()

    fun contribution(player: GamePlayer, amount: Int): PotBuilder {
        pot[player] = amount
        return this
    }

    fun build(): MutableMap<GamePlayer,Int> = pot

}
