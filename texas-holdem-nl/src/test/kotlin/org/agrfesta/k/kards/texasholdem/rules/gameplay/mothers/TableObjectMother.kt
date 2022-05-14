package org.agrfesta.k.kards.texasholdem.rules.gameplay.mothers

import org.agrfesta.k.kards.texasholdem.rules.gameplay.InGamePlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Table

const val BUTTON = "Button"
const val SMALL_BLIND = "Small Blind"
const val BIG_BLIND = "Big Blind"
const val UNDER_THE_GUN = "UTG"
const val MIDDLE = "Middle"
const val LATE = "Late"

private fun Table<InGamePlayer>.getPlayerBySeatName(player: String) =
    findPlayerByName(player) ?: throw MissingPlayerException(player)

fun Table<InGamePlayer>.middle() = getPlayerBySeatName(MIDDLE)
fun Table<InGamePlayer>.late() = getPlayerBySeatName(LATE)
fun Table<InGamePlayer>.underTheGun() = getPlayerBySeatName(UNDER_THE_GUN)
fun Table<InGamePlayer>.button() = getPlayerBySeatName(BUTTON)
fun Table<InGamePlayer>.smallBlind() = getPlayerBySeatName(SMALL_BLIND)
fun Table<InGamePlayer>.bigBlind() = getPlayerBySeatName(BIG_BLIND)

class MissingPlayerException(val player: String): Exception() {
    override val message: String
        get() = "$player is missing"
}
