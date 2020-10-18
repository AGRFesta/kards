package agrfesta.k.cards.texasholdem.rules.gameplay

class TableBuilder<T: SeatName> {
    private val players: MutableList<T> = mutableListOf()
    private var button: Int = 0

    fun withPlayers(vararg players: T): TableBuilder<T> {
        players.forEach {
            require(this.players.firstOrNull { p -> p.getSeatName() == it.getSeatName() } == null)
            { "Player '${it.getSeatName()}' is already sitting at the table!" }
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

fun <T: SeatName> buildingTable() = TableBuilder<T>()
