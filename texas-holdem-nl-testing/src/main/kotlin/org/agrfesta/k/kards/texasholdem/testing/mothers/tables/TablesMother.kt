package org.agrfesta.k.kards.texasholdem.testing.mothers.tables

import org.agrfesta.k.kards.texasholdem.rules.gameplay.InGamePlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerIdentity
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PublicInGamePlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.SittingPlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Table
import org.agrfesta.k.kards.texasholdem.rules.gameplay.TableImpl
import org.agrfesta.k.kards.texasholdem.testing.mothers.anIdentity
import org.agrfesta.k.kards.texasholdem.utils.DistinctList

// Placeholders
val smallB = anIdentity(name = "Small Blind")
val bigB = anIdentity(name = "Big Blind")
val utg = anIdentity(name = "UTG")
val utg1 = anIdentity(name = "UTG +1")
val utg2 = anIdentity(name = "UTG +2")
val middle = anIdentity(name = "Middle")
val middle1 = anIdentity(name = "Middle +1")
val middle2 = anIdentity(name = "Middle +2")
val late = anIdentity(name = "Late")
val button = anIdentity(name = "Button")

fun anInGameTable(): Table<InGamePlayer> = aTenInGamePlayerTable {}

fun aPublicInGamePlayerTable(): Table<PublicInGamePlayer> = aTenPublicPlayerTable {}

fun aSittingPlayerTable(): Table<SittingPlayer> = aTenSittingPlayerTable {}

/**
 * Create a [Table] from a distinct list of players, the first will be considered in button position.
 */
fun <T: PlayerIdentity> aPlayerTable(players: DistinctList<T>): Table<T> = TableImpl(players = players, button = 0u)
