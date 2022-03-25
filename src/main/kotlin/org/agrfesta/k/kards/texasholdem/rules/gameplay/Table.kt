package org.agrfesta.k.kards.texasholdem.rules.gameplay

import org.agrfesta.k.cards.playingcards.utils.CircularIterator
import org.agrfesta.k.cards.playingcards.utils.circularIndexMapping
import org.agrfesta.k.cards.playingcards.utils.circularIteratorFrom

class Table<T: SeatName>(val players: List<T>, val button: UInt = 0u) {

    init {
        require(players.size >= 2)
            { "The minimum number of players for a Table is 2, actual number: ${players.size}" }
        require(players.size == players.distinctBy { it.name }.size)
            { "Tables contains duplicate players!" }
    }

    fun getPlayerByPosition(position: Position): T = players[position.pos(this).toInt()]
    fun position(player: SeatName): Int = players.indices.first { players[it].name == player.name }
    fun findPlayerBySeatName(seatName: String): T? = players.firstOrNull { it.name == seatName }

    fun iterateFromSB(): CircularIterator<T> = players.circularIteratorFrom(getSBPosition(this))
    fun iterateFromUTG(): CircularIterator<T> = players.circularIteratorFrom(getUTGPosition(this))

    fun <M: SeatName> map(function: (T) -> M): Table<M> = Table(players.map(function), button)
}

private fun <T: SeatName> getSBPosition(table: Table<T>): UInt =
    table.players.circularIndexMapping(table.button + if (table.players.size == 2) 0u else 1u)
private fun <T: SeatName> getBBPosition(table: Table<T>): UInt =
    table.players.circularIndexMapping(getSBPosition(table) + 1u)
private fun <T: SeatName> getUTGPosition(table: Table<T>): UInt =
    if (table.players.size == 2) getSBPosition(table)
    else table.players.circularIndexMapping(getBBPosition(table) + 1u)

enum class Position(private val function: (Table<*>) -> UInt) {
    BUTTON({ it.button }),
    SMALL_BLIND({ getSBPosition(it) }),
    BIG_BLIND({ getBBPosition(it) }),
    UNDER_THE_GUN({ getUTGPosition(it) });

    fun pos(table: Table<*>) = function.invoke(table)
}
