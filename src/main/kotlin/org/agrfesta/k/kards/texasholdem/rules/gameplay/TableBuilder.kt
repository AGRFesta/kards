package org.agrfesta.k.kards.texasholdem.rules.gameplay

class TableBuilder<T: SeatName> {
    private val players: MutableList<T> = mutableListOf()
    private var button: Int = 0

    fun withPlayers(vararg players: T): TableBuilder<T> {
        players.forEach {
            require(this.players.firstOrNull { p -> p.name == it.name } == null)
            { "Player '${it.name}' is already sitting at the table!" }
        }
        this.players.addAll(players)
        return this
    }
    fun withPlayers(players: List<T>): TableBuilder<T> {
        players.forEach {
            require(this.players.firstOrNull { p -> p.name == it.name } == null)
            { "Player '${it.name}' is already sitting at the table!" }
        }
        this.players.addAll(players)
        return this
    }
    fun withButtonInPosition(position: Int): TableBuilder<T> {
        this.button = position
        return this
    }

    fun build(): Table<T> = Table(players, button)

}

/**
 * Build a [Table] applying the provided [setup].
 */
fun <T: SeatName> buildTable(
    setup: TableBuilder<T>.() -> Unit
): Table<T> {
    val builder = TableBuilder<T>()
    builder.setup()
    return builder.build()
}
