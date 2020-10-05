package agrfesta.k.cards.texasholdem.rules.gameplay

class Table(playersList: List<GamePlayer>, val button: Int) {
  val players: List<GamePlayer> = playersList.toList()

  init {
    if (players.size < 2) {
      throw IllegalArgumentException("The minimum number of players for a Table is 2, actual number: ${players.size}")
    }
  }

  fun getPlayer(position: Position): GamePlayer {
    return players[position.pos(this)]
  }

  fun iterateFromSB(): TableIterator = TableIterator(players, Position.SMALL_BLIND.pos(this))
  fun iterateFromUTG(): TableIterator = TableIterator(players, Position.UNDER_THE_GUN.pos(this))
}

class TableIterator(private val players: List<GamePlayer>,
                    private var actualPosition: Int) {
  fun next(): GamePlayer = players[actualPosition++ % players.size]
}

private fun getSBPosition(table: Table): Int =
    if (table.players.size == 2) (table.button % table.players.size + table.players.size) % table.players.size
    else ((table.button % table.players.size + table.players.size) % table.players.size + 1) % table.players.size

private fun getBBPosition(table: Table): Int = (getSBPosition(table) + 1) % table.players.size
private fun getUTGPosition(table: Table): Int =
    if (table.players.size == 2) getSBPosition(table)
    else (getBBPosition(table) + 1) % table.players.size

enum class Position(private val function: (Table) -> Int) {
  BUTTON({ it.button }),
  SMALL_BLIND({ getSBPosition(it) }),
  BIG_BLIND({ getBBPosition(it) }),
  UNDER_THE_GUN({ getUTGPosition(it) });

  fun pos(table: Table) = function.invoke(table)
}
