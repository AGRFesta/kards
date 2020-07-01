package agrfesta.k.cards.texasholdem.tournaments

import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import assertk.Assert
import assertk.assertions.support.expected

fun Assert<List<Set<Player>>>.theWinnerIs(player: Player) = given {
    if ( it.isEmpty() ) expected("result is empty, there is no winner!")
    if ( it[0].isEmpty() ) expected("winner's set empty, there is no winner!")
    if ( it[0].size > 1 ) expected("there is more than a winner!")
    if ( it[0].contains(player) ) return
    expected("$player is not the winner!")
}