package org.agrfesta.k.kards.texasholdem.rules.gameplay

import org.agrfesta.k.cards.playingcards.utils.CircularIterator
import org.agrfesta.k.cards.playingcards.utils.circularIndexMapping
import org.agrfesta.k.cards.playingcards.utils.circularIteratorFrom
import org.agrfesta.k.kards.texasholdem.utils.DistinctList

interface Table<T: SeatName> {
    val players: DistinctList<T>
    val button: UInt
    
    fun <M: SeatName> map(function: (T) -> M): Table<M>
    fun getPlayerFrom(position: Position): T
    fun iterateFrom(position: Position): CircularIterator<T>
    fun findPlayerByName(name: String): T?
}

class TableImpl<T: SeatName>(
    override val players: DistinctList<T>,
    override val button: UInt = 1u): Table<T> {

    init {
        require(players.size >= 2)
            { "The minimum number of players for a Table is 2, actual number: ${players.size}" }
    }

    override fun getPlayerFrom(position: Position): T = players[getIndexFromPosition(position).toInt()]
    fun position(player: SeatName): UInt = players.indexOf(player)
        .let {
            if (it == -1) throw IllegalArgumentException("Player ${player.name} is not sitting at the table")
            else it.toUInt()
        }
    override fun findPlayerByName(name: String): T? = players.firstOrNull { it.name == name }

    override fun iterateFrom(position: Position): CircularIterator<T> =
        players.circularIteratorFrom(getIndexFromPosition(position))

    override fun <M: SeatName> map(function: (T) -> M): Table<M> =
        TableImpl(players.map(function), button)

    private fun getIndexFromPosition(position: Position): UInt = when (position) {
        Position.BUTTON -> button
        Position.SMALL_BLIND -> getSBPosition()
        Position.BIG_BLIND -> getBBPosition()
        Position.UNDER_THE_GUN -> getUTGPosition()
    }
    private fun getSBPosition(): UInt = players.circularIndexMapping(button + if (players.size == 2) 0u else 1u)
    private fun getBBPosition(): UInt = players.circularIndexMapping(getSBPosition() + 1u)
    private fun getUTGPosition(): UInt =
        if (players.size == 2) getSBPosition() else players.circularIndexMapping(getBBPosition() + 1u)
}

enum class Position {BUTTON, SMALL_BLIND, BIG_BLIND, UNDER_THE_GUN}
