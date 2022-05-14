package org.agrfesta.k.kards.texasholdem.testing.mothers

import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.cards.playingcards.suits.createFrenchCard
import org.agrfesta.k.cards.playingcards.suits.createFrenchHand
import org.agrfesta.k.cards.playingcards.suits.frenchCardsSet
import org.agrfesta.k.kards.texasholdem.rules.gameplay.InGamePlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.InGamePlayerImpl
import org.agrfesta.k.kards.texasholdem.rules.gameplay.OwnPlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.OwnPlayerImpl
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Player
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerIdentity
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStrategyInterface
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PublicInGamePlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PublicInGamePlayerImpl
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PublicSittingPlayerImpl
import org.agrfesta.k.kards.texasholdem.rules.gameplay.SittingPlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.identifies
import org.agrfesta.k.kards.texasholdem.rules.gameplay.playingAs
import org.agrfesta.k.kards.texasholdem.rules.gameplay.sittingWith
import org.agrfesta.k.kards.texasholdem.testing.mothers.CircularStrategy.Companion.aStrategy
import java.util.*

// Cards ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
val aPlayerCardsSet = frenchCardsSet("7s","2c")
fun cards(vararg strings: String): Array<Card> = frenchCardsSet(*strings).toTypedArray()
fun card(string: String): Card = createFrenchCard(string)
fun cardList(vararg strings: String): List<Card> = createFrenchHand(*strings)
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///// Players //////////////////////////////////////////////////////////////////////////////////////////////////////////
val alex = aPlayer(name = "Alex")
val poly = aPlayer(name = "Poly")
val jane = aPlayer(name = "Jane")
val dave = aPlayer(name = "Dave")
val maya = aPlayer(name = "Maya")
val juno = aPlayer(name = "Juno")

val foldedPlayer: InGamePlayer = anInGamePlayer(name = "FoldedPlayer", status = PlayerStatus.FOLD)
val allInPlayer: InGamePlayer = anInGamePlayer(name = "AllInPlayer", status = PlayerStatus.ALL_IN)
val callingPlayer: InGamePlayer = anInGamePlayer(name = "CallingPlayer", status = PlayerStatus.CALL)
val raisingPlayer: InGamePlayer = anInGamePlayer(name = "RaisingPlayer", status = PlayerStatus.RAISE)

fun anIdentity(uuid: UUID = UUID.randomUUID(), name: String = "aName"): PlayerIdentity = uuid identifies name

fun aPlayer(uuid: UUID = UUID.randomUUID(), name: String = "aName", strategy: PlayerStrategyInterface = aStrategy()):
        Player = uuid identifies name playingAs strategy

fun aPublicInGamePlayer(
    uuid: UUID = UUID.randomUUID(),
    name: String = "aName",
    stack: UInt = UInt.MAX_VALUE,
    status: PlayerStatus = PlayerStatus.NONE
): PublicInGamePlayer =
    PublicInGamePlayerImpl(PublicSittingPlayerImpl(uuid identifies name, stack), status)
fun aPublicInGamePlayer(
    identity: PlayerIdentity,
    stack: UInt = UInt.MAX_VALUE,
    status: PlayerStatus = PlayerStatus.NONE
): PublicInGamePlayer =
    PublicInGamePlayerImpl(PublicSittingPlayerImpl(identity, stack), status)

fun aSittingPlayer(player: Player, stack: UInt = 0u): SittingPlayer = player sittingWith stack
fun aSittingPlayer(
    uuid: UUID = UUID.randomUUID(),
    name: String = "aName",
    stack: UInt = 0u,
    strategy: PlayerStrategyInterface = aStrategy()): SittingPlayer =
    uuid identifies name playingAs strategy sittingWith stack
fun aSittingPlayer(
    identity: PlayerIdentity,
    stack: UInt = 0u,
    strategy: PlayerStrategyInterface = aStrategy()): SittingPlayer =
    identity playingAs strategy sittingWith stack

fun anInGamePlayer(
    uuid: UUID = UUID.randomUUID(),
    name: String = "aName",
    stack: UInt = 0u,
    strategy: PlayerStrategyInterface = aStrategy(),
    status: PlayerStatus = PlayerStatus.FOLD,
    cards: Set<Card> = aPlayerCardsSet): InGamePlayer =
    InGamePlayerImpl(uuid identifies name playingAs strategy sittingWith stack, status, cards)
fun anInGamePlayer(
    identity: PlayerIdentity,
    stack: UInt = 0u,
    strategy: PlayerStrategyInterface = aStrategy(),
    status: PlayerStatus = PlayerStatus.FOLD,
    cards: Set<Card> = aPlayerCardsSet): InGamePlayer =
    InGamePlayerImpl(identity playingAs strategy sittingWith stack, status, cards)

fun anOwnPlayer(
    uuid: UUID = UUID.randomUUID(),
    name: String = "aName",
    stack: UInt = 0u,
    status: PlayerStatus = PlayerStatus.NONE,
    cards: Set<Card> = aPlayerCardsSet): OwnPlayer =
    OwnPlayerImpl(aPublicInGamePlayer(uuid, name, stack, status), cards)
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
