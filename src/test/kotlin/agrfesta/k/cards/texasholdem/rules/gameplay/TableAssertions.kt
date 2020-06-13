package agrfesta.k.cards.texasholdem.rules.gameplay

import assertk.Assert
import assertk.assertions.support.expected

fun Assert<Player>.isSittingOn(table: Table, position: Position) = given {
    assertThat(it).isSittingAt(table)
    if ( it == table.getPlayer(position).player ) return
    expected("$it is not sitting on $position")
}

fun Assert<Player>.isSittingAt(table: Table) = given {
    if ( table.players.find { gp -> gp.player == it } != null ) return
    expected("$it is not sitting at table $table")
}

