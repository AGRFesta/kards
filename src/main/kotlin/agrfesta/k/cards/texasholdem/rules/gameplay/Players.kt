package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.texasholdem.playercontext.PlayerGameContext

data class Player(val name: String, val strategy: PlayerStrategyInterface) {
    override fun toString() = "$name{$strategy}"
}

class Opponent(val name: String, val stack: Int, val status: PlayerStatus) {
    override fun toString() = "$name[$stack]"
}

class InGamePlayer(
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
        require(amount >= 0) { "Can't receive a negative amount" }
        stack += amount
    }

    fun asOpponent(): Opponent = Opponent(name, stack, status)

    fun pay(amount: Int): Int {
        require(amount >= 0) { "Can't pay a negative amount" }
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

fun List<InGamePlayer>.resetActivePlayersStatus() = this.getActive()
        .forEach { it.status = PlayerStatus.NONE }
fun List<InGamePlayer>.getActive(): List<InGamePlayer> = this.filter { it.isActive() }

fun List<InGamePlayer>.findWinner(): InGamePlayer? {
    val notFoldedPlayers = this.filter { !it.hasFolded() }
    return if (notFoldedPlayers.size == 1) notFoldedPlayers[0]
    else null
}

fun List<InGamePlayer>.get(player: Player): InGamePlayer? = this.find { it.player == player }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
