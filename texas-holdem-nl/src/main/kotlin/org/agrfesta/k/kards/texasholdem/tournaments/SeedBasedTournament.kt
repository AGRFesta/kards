package org.agrfesta.k.kards.texasholdem.tournaments

import org.agrfesta.k.cards.playingcards.utils.SimpleRandomGenerator
import org.agrfesta.k.kards.texasholdem.observers.TournamentObserver
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Player
import java.util.*
import kotlin.random.Random

typealias ButtonProvider = (Int) -> UInt

fun tournamentBySeed(
    seed: Long,
    descriptor: TournamentDescriptor,
    subscriptions: Set<Player>,
    uuid: UUID = UUID.randomUUID(),
    observer: TournamentObserver? = null,
    gameProvider: (Random) -> GameProvider = { gameProviderRandomBased(it) },
    buttonProvider: (Random) -> ButtonProvider = { rnd -> { SimpleRandomGenerator(rnd).nextInt(it).toUInt()} } ):
        Tournament {
    val random = Random(seed)
    return TournamentImpl(uuid, descriptor, subscriptions, observer, gameProvider(random), buttonProvider(random))
}
