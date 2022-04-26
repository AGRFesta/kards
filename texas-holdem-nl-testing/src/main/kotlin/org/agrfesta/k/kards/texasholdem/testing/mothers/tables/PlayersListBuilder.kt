package org.agrfesta.k.kards.texasholdem.testing.mothers.tables

import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerIdentity
import org.agrfesta.k.kards.texasholdem.utils.DistinctList
import org.agrfesta.k.kards.texasholdem.utils.DistinctList.Companion.distinctListOf

class PlayersListBuilder<T: PlayerIdentity> private constructor(private val initialList: List<T>) {
    private val replacements = mutableMapOf<PlayerIdentity, T>()

    companion object {
        fun <T: PlayerIdentity> DistinctList<T>.replacing(setup: PlayersListBuilder<T>.() -> Unit): DistinctList<T> {
            val builder = PlayersListBuilder(this)
            builder.setup()
            return builder.build()
        }
    }

    infix fun T.sittingAt(placeholder: PlayerIdentity) {
        require(initialList.contains(placeholder))
            { "Unable to find '${placeholder.name}' placeholder in initial list: ${initialList.map { it.name }}" }
        replacements[placeholder] = this
    }

    infix fun PlayerIdentity.with(mapper: (PlayerIdentity) -> T) {
        require(initialList.contains(this))
            { "Unable to find '${this.name}' placeholder in initial list: ${initialList.map { it.name }}" }
        replacements[this] = mapper(this)
    }

    private fun build(): DistinctList<T> = distinctListOf(initialList.map { replacements[it] ?: it })
}