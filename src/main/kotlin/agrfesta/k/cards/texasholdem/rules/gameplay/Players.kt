package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.kcards.playingcards.cards.Card

class Player(
        val name: String,
        var stack: Int,
        strategyProvider: (p: Player) -> PlayerStrategyInterface
    ): PlayerStrategyInterface {
    var status: PlayerStatus = PlayerStatus.NONE
    var cards: Set<Card> = setOf()
    private val strategy = strategyProvider.invoke(this)

    /// A Player that is out of the Game
    fun hasFolded(): Boolean = status == PlayerStatus.FOLD

    /// A Player that can still take part to the Game
    fun isActive(): Boolean = status!=PlayerStatus.FOLD && status!=PlayerStatus.ALL_IN

    fun receive(amount: Int) {
        if (amount < 0) throw IllegalArgumentException("Can't receive a negative amount")
        stack += amount
    }

    fun pay(amount: Int): Int {
        if (amount < 0) throw IllegalArgumentException("Can't pay a negative amount")
        val effectiveAmount = amount.coerceAtMost(stack)
        stack -= effectiveAmount
        if (stack == 0) {
            status = PlayerStatus.ALL_IN
        }
        return effectiveAmount
    }

    override fun act(context: GameContext): Action = strategy.act(context)

    override fun toString(): String = "$name[$strategy] ($stack)"

}

enum class PlayerStatus {
    ALL_IN, FOLD, CALL, RAISE, NONE
}

interface PlayerStrategyInterface {
    fun act(context: GameContext): Action
}

/// List<Player> ///////////////////////////////////////////////////////////////////////////////////////////////////////

fun List<Player>.resetActivePlayersStatus() = this.getActive()
        .forEach { it.status = PlayerStatus.NONE }
fun List<Player>.getActive(): List<Player> = this.filter { it.isActive() }

fun List<Player>.findWinner(): Player? {
    val notFoldedPlayers = this.filter { !it.hasFolded() }
    return if (notFoldedPlayers.size == 1) notFoldedPlayers[0]
    else null
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////