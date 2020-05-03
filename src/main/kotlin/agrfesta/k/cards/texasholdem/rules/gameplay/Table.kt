package agrfesta.k.cards.texasholdem.rules.gameplay

class Table(val players: List<GamePlayer>, private val button: Int) {
    private val size = players.size

    init {
        if (size < 2) {
            throw IllegalArgumentException("The minimum number of players for a Table is 2, actual number: $size")
        }
    }

    private fun getSBPosition(): Int = if (size == 2) (button%size + size)%size
                                else ((button%size + size)%size + 1) % size
    private fun getBBPosition(): Int = (getSBPosition() + 1) % size
    private fun getUTGPosition(): Int = if (size == 2) getSBPosition()
                        else (getBBPosition() + 1) % size

    fun getSB(): GamePlayer = players[getSBPosition()]
    fun getBB(): GamePlayer = players[getBBPosition()]

    fun iterateFromSB(): TableIterator = TableIterator(players, getSBPosition())
    fun iterateFromUTG(): TableIterator = TableIterator(players, getUTGPosition())
}

class TableIterator(private val players: List<GamePlayer>,
                    private var actualPosition: Int) {
    fun next(): GamePlayer = players[actualPosition++%players.size]
}