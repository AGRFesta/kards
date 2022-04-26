package org.agrfesta.k.kards.texasholdem.tournaments

import assertk.Assert
import assertk.assertions.support.expected
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Player

fun Assert<List<Set<Player>>>.theWinnerIs(player: Player) = given {
    if ( it.isEmpty() ) expected("result is empty, there is no winner!")
    if ( it[0].isEmpty() ) expected("winner's set empty, there is no winner!")
    if ( it[0].size > 1 ) expected("there is more than a winner!")
    if ( it[0].contains(player) ) return
    expected("$player is not the winner!")
}
