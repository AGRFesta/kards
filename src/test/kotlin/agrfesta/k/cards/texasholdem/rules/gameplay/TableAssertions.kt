package agrfesta.k.cards.texasholdem.rules.gameplay

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.support.expected

fun <T: SeatName> Assert<SeatName>.isSittingOn(table: Table<T>, position: Position) = given {
    assertThat(it).isSittingAt(table)
    if ( it.getSeatName() == table.getPlayerByPosition(position).getSeatName() ) return
    expected("$it is not sitting on $position")
}

fun <T: SeatName> Assert<SeatName>.isSittingAt(table: Table<T>) = given {
    if ( table.players.firstOrNull { p -> p.getSeatName() == it.getSeatName() } != null ) return
    expected("$it is not sitting at table $table")
}

fun <T: SeatName, S: SeatName> Assert<Table<T>>.hasSameContentOf(expectedTable: Table<S>) {
    val expectedPlayers: List<String> = expectedTable.players.map { it.getSeatName() }
    this.given { assertThat(it.players.map(SeatName::getSeatName)).isEqualTo(expectedPlayers)}
    this.given { assertThat(it.button).isEqualTo(expectedTable.button)}
}
