package agrfesta.k.cards.texasholdem.rules.gameplay

fun getSBPosition(btt: Int, players: Int): Int = if (players == 2) (btt%players + players)%players
                                                    else ((btt%players + players)%players + 1) % players
fun getBBPosition(btt: Int, players: Int): Int = (getSBPosition(btt, players) + 1) % players
fun getUTGPosition(btt: Int, players: Int): Int = if (players == 2) getSBPosition(btt, players)
                                                    else (getBBPosition(btt, players) + 1) % players


