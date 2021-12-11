package org.agrfesta.k.kards.texasholdem.rules.gameplay

interface GamePayments {
    fun sb(): Int
    fun bb(): Int
    fun ante(): Int?
}

class GamePaymentsFixedImpl(private val sb: Int, private val bb: Int, private val ante: Int?) : GamePayments {
    constructor(sb: Int, bb: Int): this(sb,bb,null)

    override fun sb(): Int = sb
    override fun bb(): Int = bb
    override fun ante(): Int? = ante
}
