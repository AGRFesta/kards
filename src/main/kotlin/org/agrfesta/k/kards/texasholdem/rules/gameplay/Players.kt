package org.agrfesta.k.kards.texasholdem.rules.gameplay

import org.agrfesta.k.cards.playingcards.cards.Card
import java.util.*

typealias PlayerStrategyInterface = (OwnPlayer, GameContext) -> Action

/**
 * Represents a player identity.
 * Used to identify any player model through the uuid.
 */
interface PlayerIdentity {
    val uuid: UUID
    val name: String
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}
class PlayerIdentityImpl(
    override val uuid: UUID = UUID.randomUUID(),
    override val name: String): PlayerIdentity {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PlayerIdentity) return false
        if (uuid != other.uuid) return false
        return true
    }

    override fun hashCode(): Int = uuid.hashCode()

    override fun toString(): String = "$name($uuid)"
}

/**
 * Creates a player identity binding the name with the uuid.
 */
infix fun UUID.identifies(name: String): PlayerIdentity = PlayerIdentityImpl(this, name)

/**
 * Represents a player with its strategy.
 */
interface Player: PlayerIdentity {
    /**
     * Returns the chosen [Action] based on [hero] and [context].
     */
    fun act(hero: OwnPlayer, context: GameContext): Action
}
class PlayerImpl(identity: PlayerIdentity, private val strategy: PlayerStrategyInterface):
    Player, PlayerIdentity by identity {
    override fun act(hero: OwnPlayer, context: GameContext): Action = strategy(hero, context)
    override fun toString(): String = name
}

/**
 * Creates a [Player] defining the [strategy] of and identified player.
 */
infix fun PlayerIdentity.playingAs(strategy: PlayerStrategyInterface): Player = PlayerImpl(this, strategy)

/**
 * Represents the public information of a player sitting with a stack at the table.
 */
interface PublicSittingPlayer: PlayerIdentity {
    val stack: UInt
}
class PublicSittingPlayerImpl(
    player: PlayerIdentity,
    override val stack: UInt
): PublicSittingPlayer, PlayerIdentity by player {
    override fun toString(): String = "$name($stack)"
}

/**
 * Represents the public information of a player in game.
 */
interface PublicInGamePlayer: PublicSittingPlayer {
    val status: PlayerStatus
}
class PublicInGamePlayerImpl(
    player: PublicSittingPlayer,
    override val status: PlayerStatus = PlayerStatus.NONE
): PublicInGamePlayer, PublicSittingPlayer by player {
    override fun toString(): String = "$name($stack)[$status]"
}

/**
 * Represents a player sitting with a stack at the table.
 */
interface SittingPlayer: Player {
    val stack: UInt
    fun asIdentity(): PlayerIdentity
    fun asPublicSittingPlayer(): PublicSittingPlayer
}
class SittingPlayerImpl(player: Player, override val stack: UInt): SittingPlayer, Player by player {
    override fun asIdentity(): PlayerIdentity = PlayerIdentityImpl(uuid, name)
    override fun asPublicSittingPlayer(): PublicSittingPlayer = PublicSittingPlayerImpl(asIdentity(), stack)
    override fun toString(): String = "$name($stack)"
}

/**
 * Creates a [SittingPlayer] from a [Player] sitting with [stack].
 */
infix fun Player.sittingWith(stack: UInt): SittingPlayer = SittingPlayerImpl(this, stack)

infix fun Player.owns(stack: UInt): SittingPlayer = SittingPlayerImpl(this, stack)
fun Collection<SittingPlayer>.toRanking() = map { it.asPublicSittingPlayer() }
    .sortedByDescending { it.stack }

/**
 * Represents own player information.
 */
interface OwnPlayer: PublicInGamePlayer {
    val cards: Set<Card>
}
class OwnPlayerImpl(
    publicPlayer: PublicInGamePlayer,
    override val cards: Set<Card>
): OwnPlayer, PublicInGamePlayer by publicPlayer

/**
 * Represents an in game player.
 */
interface InGamePlayer: SittingPlayer {
    val cards: Set<Card>
    override var stack: UInt
    var status: PlayerStatus

    fun hasFolded(): Boolean
    fun isActive(): Boolean

    fun pay(amount: UInt): UInt
    fun receive(amount: UInt)

    fun asPlayer(): Player
    fun asSittingPlayer(): SittingPlayer
    fun asPublicInGamePlayer(): PublicInGamePlayer
    fun asOwnPlayer(): OwnPlayer

    fun calculateAmountToCall(pot: Pot): UInt
}
class InGamePlayerImpl(
    sittingPlayer: SittingPlayer,
    override var status: PlayerStatus = PlayerStatus.NONE,
    override val cards: Set<Card>): InGamePlayer, SittingPlayer by sittingPlayer {
    override var stack: UInt = sittingPlayer.stack

    init {
        require(cards.size == 2) { "Must hold two cards, received ${cards.size}" }
    }

    /// A Player that is out of the Game
    override fun hasFolded(): Boolean = status == PlayerStatus.FOLD

    /// A Player that can still take part to the Game
    override fun isActive(): Boolean = status!=PlayerStatus.FOLD && status!=PlayerStatus.ALL_IN

    override fun asPlayer(): Player = PlayerImpl(asIdentity(), this::act)
    override fun asSittingPlayer(): SittingPlayer = SittingPlayerImpl(asPlayer(), stack)
    override fun asPublicSittingPlayer(): PublicSittingPlayer = PublicSittingPlayerImpl(asIdentity(), stack)
    override fun asPublicInGamePlayer(): PublicInGamePlayer = PublicInGamePlayerImpl(asPublicSittingPlayer(), status)
    override fun asOwnPlayer(): OwnPlayer = OwnPlayerImpl(asPublicInGamePlayer(), cards)

    override fun receive(amount: UInt) { stack += amount }

    override fun pay(amount: UInt): UInt {
        val effectiveAmount = amount.coerceAtMost(stack)
        stack -= effectiveAmount
        if (stack == 0u) {
            status = PlayerStatus.ALL_IN
        }
        return effectiveAmount
    }

    override fun calculateAmountToCall(pot: Pot): UInt =
        (pot.maxContribution()?.amount ?: 0u) - pot.payedBy(this)
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
