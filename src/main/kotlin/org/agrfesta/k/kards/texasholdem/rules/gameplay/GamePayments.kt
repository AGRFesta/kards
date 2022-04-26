package org.agrfesta.k.kards.texasholdem.rules.gameplay

interface GamePayments {
    val sb: UInt
    val bb: UInt
    val ante: UInt?
}

class GamePaymentsFixedImpl(override val sb: UInt, override val bb: UInt, override val ante: UInt? = null): GamePayments
