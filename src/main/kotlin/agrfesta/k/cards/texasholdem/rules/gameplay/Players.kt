package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.texasholdem.playercontext.PlayerGameContext

data class Player(val name: String, val strategy: PlayerStrategyInterface) {
    override fun toString() = "$name{$strategy}"
}

class GamePlayer(
        val player: Player,
        var stack: Int
    ): PlayerStrategyInterface {
    val name = player.name
    var status: PlayerStatus = PlayerStatus.NONE

    var cards: Set<Card> = setOf() //TODO check that are exactly two

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

    fun asOwnPlayer() = OwnPlayer(name, cards, stack)

    override fun act(context: PlayerGameContext): Action = player.strategy.act(context)
    override fun toString(): String = "$player ($stack)"

}

enum class PlayerStatus {
    ALL_IN, FOLD, CALL, RAISE, NONE
}

interface PlayerStrategyInterface {
    fun act(context: PlayerGameContext): Action
}

class OwnPlayer(val name: String, val cards: Set<Card>, val stack: Int)

/// List<Player> ///////////////////////////////////////////////////////////////////////////////////////////////////////

fun List<GamePlayer>.resetActivePlayersStatus() = this.getActive()
        .forEach { it.status = PlayerStatus.NONE }
fun List<GamePlayer>.getActive(): List<GamePlayer> = this.filter { it.isActive() }

fun List<GamePlayer>.findWinner(): GamePlayer? {
    val notFoldedPlayers = this.filter { !it.hasFolded() }
    return if (notFoldedPlayers.size == 1) notFoldedPlayers[0]
    else null
}

fun List<GamePlayer>.get(player: Player): GamePlayer? = this.find { it.player == player }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
