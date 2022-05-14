package org.agrfesta.k.kards.texasholdem.rules.gameplay

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.support.expected

fun <T: PlayerIdentity> Assert<PlayerIdentity>.isSittingOn(table: Table<T>, position: Position) = given {
    assertThat(it).isSittingAt(table)
    if ( it.name == table.getPlayerFrom(position).name ) return
    expected("$it is not sitting on $position")
}

fun <T: PlayerIdentity> Assert<PlayerIdentity>.isSittingAt(table: Table<T>) = given {
    if ( table.players.firstOrNull { p -> p.name == it.name } != null ) return
    expected("$it is not sitting at table $table")
}

fun <T: PlayerIdentity, S: PlayerIdentity> Assert<Table<T>>.hasSameContentOf(expectedTable: Table<S>) {
    val expectedPlayers: List<String> = expectedTable.players.map { it.name }
    this.given { assertThat(it.players.map(PlayerIdentity::name)).isEqualTo(expectedPlayers)}
    this.given { assertThat(it.button).isEqualTo(expectedTable.button)}
}
