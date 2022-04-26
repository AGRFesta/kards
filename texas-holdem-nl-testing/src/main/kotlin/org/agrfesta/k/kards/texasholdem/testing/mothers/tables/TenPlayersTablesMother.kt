package org.agrfesta.k.kards.texasholdem.testing.mothers.tables

import org.agrfesta.k.kards.texasholdem.rules.gameplay.InGamePlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PublicInGamePlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.SittingPlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Table
import org.agrfesta.k.kards.texasholdem.testing.mothers.aPublicInGamePlayer
import org.agrfesta.k.kards.texasholdem.testing.mothers.aSittingPlayer
import org.agrfesta.k.kards.texasholdem.testing.mothers.anInGamePlayer
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.PlayersListBuilder.Companion.replacing
import org.agrfesta.k.kards.texasholdem.utils.DistinctList.Companion.distinctListOf

val tenPlayersTableList = distinctListOf(button, smallB, bigB, utg, utg1, utg2, middle, middle1, middle2, late)

fun aTenInGamePlayerTable(setup: PlayersListBuilder<InGamePlayer>.() -> Unit): Table<InGamePlayer> =
    aPlayerTable(tenPlayersTableList
        .map { anInGamePlayer(it) }
        .replacing(setup))

fun aTenPublicPlayerTable(setup: PlayersListBuilder<PublicInGamePlayer>.() -> Unit): Table<PublicInGamePlayer> =
    aPlayerTable(tenPlayersTableList
        .map { aPublicInGamePlayer(it) }
        .replacing(setup))

fun aTenSittingPlayerTable(setup: PlayersListBuilder<SittingPlayer>.() -> Unit): Table<SittingPlayer> =
    aPlayerTable(tenPlayersTableList
        .map { aSittingPlayer(it) }
        .replacing(setup))

