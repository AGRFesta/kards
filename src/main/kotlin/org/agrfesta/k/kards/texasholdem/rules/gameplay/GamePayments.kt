package org.agrfesta.k.kards.texasholdem.rules.gameplay

interface GamePayments {
    fun sb(): UInt
    fun bb(): UInt
    fun ante(): UInt?
}

class GamePaymentsFixedImpl(private val sb: UInt, private val bb: UInt, private val ante: UInt?) : GamePayments {
    constructor(sb: UInt, bb: UInt): this(sb,bb,null)

    override fun sb(): UInt = sb
    override fun bb(): UInt = bb
    override fun ante(): UInt? = ante
}
