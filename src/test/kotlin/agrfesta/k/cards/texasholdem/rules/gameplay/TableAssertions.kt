package agrfesta.k.cards.texasholdem.rules.gameplay

import assertk.Assert
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

