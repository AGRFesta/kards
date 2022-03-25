package org.agrfesta.k.kards.texasholdem.rules.gameplay

import org.agrfesta.k.cards.playingcards.cards.Card

typealias PlayerStrategyInterface = (HeroGameContextImpl<OwnPlayer>) -> Action

interface SeatName {
    val name: String
}
interface SeatNameStack: SeatName {
    val stack: UInt
}

data class Player(override val name: String, val strategy: PlayerStrategyInterface): SeatName {
    override fun toString() = "$name{$strategy}"
}

class Opponent(override val name: String, override val stack: UInt, val status: PlayerStatus): SeatNameStack {
    override fun toString() = "$name[$stack]"
}
data class PlayerStack(val player: Player, override val stack: UInt): SeatNameStack {
    override val name: String = player.name
    override fun toString() = "${player.name}[$stack]"
}
infix fun Player.owns(stack: UInt) = PlayerStack(this, stack)
fun Collection<PlayerStack>.toRanking() = sortedByDescending { it.stack }

class OwnPlayer(
    override val name: String,
    val cards: Set<Card>,
    override val stack: UInt,
    val amountToCall: UInt)
    : SeatNameStack

class OpponentHero(override val name: String, override val stack: UInt, val cards: Set<Card>? = null): SeatNameStack

class InGamePlayer(val player: Player, override var stack: UInt, val cards: Set<Card>)
    : PlayerStrategyInterface by player.strategy, SeatNameStack {
    override val name = player.name

    var status: PlayerStatus = PlayerStatus.NONE

    init {
        require(cards.size == 2) { "Must hold two cards, received ${cards.size}" }
    }

    /// A Player that is out of the Game
    fun hasFolded(): Boolean = status == PlayerStatus.FOLD

    /// A Player that can still take part to the Game
    fun isActive(): Boolean = status!=PlayerStatus.FOLD && status!=PlayerStatus.ALL_IN

    fun receive(amount: UInt) { stack += amount }

    fun asOpponent(): Opponent = Opponent(name, stack, status)
    fun asPlayerStack(): PlayerStack = PlayerStack(player, stack)

    fun pay(amount: UInt): UInt {
        val effectiveAmount = amount.coerceAtMost(stack)
        stack -= effectiveAmount
        if (stack == 0u) {
            status = PlayerStatus.ALL_IN
        }
        return effectiveAmount
    }

    fun asOwnPlayer(actualPot: InGamePot) = OwnPlayer(name, cards, stack, calculateAmountToCall(actualPot))

    fun calculateAmountToCall(pot: InGamePot): UInt = (pot.maxContribution()?.amount ?: 0u) - pot.payedBy(this)

    override fun toString(): String = "$player ($stack)"

}

enum class PlayerStatus {
    ALL_IN, FOLD, CALL, RAISE, NONE
}

/// List<Player> ///////////////////////////////////////////////////////////////////////////////////////////////////////

fun List<InGamePlayer>.resetActivePlayersStatus() = getActive()
        .forEach { it.status = PlayerStatus.NONE }
fun List<InGamePlayer>.getActive(): List<InGamePlayer> = filter { it.isActive() }

fun List<InGamePlayer>.findWinner(): InGamePlayer? {
    val notFoldedPlayers = filter { !it.hasFolded() }
    return if (notFoldedPlayers.size == 1) notFoldedPlayers[0]
    else null
}

fun List<InGamePlayer>.toPlayerStack(): List<PlayerStack> = map { it.player owns it.stack }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
