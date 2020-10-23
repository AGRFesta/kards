package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.texasholdem.playercontext.PlayerGameContext

data class Player(val name: String, val strategy: PlayerStrategyInterface): SeatName {
    override fun toString() = "$name{$strategy}"
    override fun getSeatName() = name
}

interface SeatName {
    fun getSeatName(): String
}

class Opponent(val name: String, val stack: Int, val status: PlayerStatus): SeatName {
    override fun toString() = "$name[$stack]"
    override fun getSeatName() = name
}
class PlayerStack(val player: Player, val stack: Int): SeatName {
    override fun toString() = "${player.name}[$stack]"
    override fun getSeatName() = player.getSeatName()
}
fun Collection<PlayerStack>.toRanking() = this.sortedByDescending { it.stack }

class InGamePlayer(val player: Player, var stack: Int, val cards: Set<Card>): PlayerStrategyInterface, SeatName {
    val name = player.name

    var status: PlayerStatus = PlayerStatus.NONE

    init {
        require(cards.size == 2) { "An InGamePlayer must hold two cards, received ${cards.size}" }
    }

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
    override fun getSeatName() = name

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
fun List<InGamePlayer>.toPlayerStack(): List<PlayerStack> = this.map { PlayerStack(it.player,it.stack) }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
