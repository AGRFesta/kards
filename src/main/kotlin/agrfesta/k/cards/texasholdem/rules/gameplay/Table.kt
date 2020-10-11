package agrfesta.k.cards.texasholdem.rules.gameplay

class Table<T>(playersList: List<T>, val button: Int) {
    val players: List<T> = playersList.toList()

    init {
        require(players.size >= 2) { "The minimum number of players for a Table is 2, actual number: ${players.size}" }
    }

    fun getPlayer(position: Position): T {
        return players[position.pos(this)]
    }

    fun iterateFromSB(): TableIterator<T> = TableIterator(players, Position.SMALL_BLIND.pos(this))
    fun iterateFromUTG(): TableIterator<T> = TableIterator(players, Position.UNDER_THE_GUN.pos(this))

    fun <M> map(function: (T) -> M): Table<M> = Table(players.map(function), button)
}

class TableIterator<T>(private val players: List<T>,
                       private var actualPosition: Int) {
    fun next(): T = players[actualPosition++ % players.size]
}

private fun <T> getSBPosition(table: Table<T>): Int =
        if (table.players.size == 2) (table.button % table.players.size + table.players.size) % table.players.size
        else ((table.button % table.players.size + table.players.size) % table.players.size + 1) % table.players.size

private fun <T> getBBPosition(table: Table<T>): Int = (getSBPosition(table) + 1) % table.players.size
private fun <T> getUTGPosition(table: Table<T>): Int =
        if (table.players.size == 2) getSBPosition(table)
        else (getBBPosition(table) + 1) % table.players.size

enum class Position(private val function: (Table<*>) -> Int) {
    BUTTON({ it.button }),
    SMALL_BLIND({ getSBPosition(it) }),
    BIG_BLIND({ getBBPosition(it) }),
    UNDER_THE_GUN({ getUTGPosition(it) });

    fun pos(table: Table<*>) = function.invoke(table)
}
