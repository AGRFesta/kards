package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card

interface SeatName {
    val name: String
}
interface SeatNameStack: SeatName {
    val stack: Int
}

data class Player(override val name: String, val strategy: PlayerStrategyInterface): SeatName {
    override fun toString() = "$name{$strategy}"
}

class Opponent(override val name: String, override val stack: Int, val status: PlayerStatus): SeatNameStack {
    override fun toString() = "$name[$stack]"
}
data class PlayerStack(val player: Player, override val stack: Int): SeatNameStack {
    override val name: String = player.name
    override fun toString() = "${player.name}[$stack]"
}
infix fun Player.owns(stack: Int) = PlayerStack(this, stack)
fun Collection<PlayerStack>.toRanking() = this.sortedByDescending { it.stack }

class OwnPlayer(override val name: String, val cards: Set<Card>, override val stack: Int, val amountToCall: Int)
    : SeatNameStack

class OpponentHero(override val name: String, override val stack: Int, val cards: Set<Card>? = null): SeatNameStack

class InGamePlayer(val player: Player, override var stack: Int, val cards: Set<Card>)
    : PlayerStrategyInterface, SeatNameStack {
    override val name = player.name

    var status: PlayerStatus = PlayerStatus.NONE

    init {
        require(cards.size == 2) { "Must hold two cards, received ${cards.size}" }
        require(stack >= 0) { "Can't have a negative stack, received $stack" }
    }

    /// A Player that is out of the Game
    fun hasFolded(): Boolean = status == PlayerStatus.FOLD

    /// A Player that can still take part to the Game
    fun isActive(): Boolean = status!=PlayerStatus.FOLD && status!=PlayerStatus.ALL_IN

    fun receive(amount: Int) {
        require(amount >= 0) { "Can't have a negative stack, received $amount" }
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

    fun asOwnPlayer(actualPot: InGamePot) = OwnPlayer(name, cards, stack, calculateAmountToCall(actualPot))

    fun calculateAmountToCall(pot: InGamePot): Int = (pot.maxContribution()?.amount ?: 0) - pot.payedBy(this)

    override fun act(context: ActGameContext): Action = player.strategy.act(context)

    override fun toString(): String = "$player ($stack)"

}

enum class PlayerStatus {
    ALL_IN, FOLD, CALL, RAISE, NONE
}

interface PlayerStrategyInterface {
    fun act(context: ActGameContext): Action
}

/// List<Player> ///////////////////////////////////////////////////////////////////////////////////////////////////////

fun List<InGamePlayer>.resetActivePlayersStatus() = this.getActive()
        .forEach { it.status = PlayerStatus.NONE }
fun List<InGamePlayer>.getActive(): List<InGamePlayer> = this.filter { it.isActive() }

fun List<InGamePlayer>.findWinner(): InGamePlayer? {
    val notFoldedPlayers = this.filter { !it.hasFolded() }
    return if (notFoldedPlayers.size == 1) notFoldedPlayers[0]
    else null
}

fun List<InGamePlayer>.toPlayerStack(): List<PlayerStack> = this.map { it.player owns it.stack }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
