package org.agrfesta.k.kards.texasholdem.rules.gameplay

enum class ActionType {
    Call, Raise, Fold
}

interface Action {
    val amount: UInt?
    val type: ActionType
}

data class ActionImpl(
    override val type: ActionType,
    override val amount: UInt? = null): Action {
    override fun toString() = "$type${ if (amount!=null) " $amount" else "" }"
}
fun fold() = ActionImpl(ActionType.Fold)
fun call() = ActionImpl(ActionType.Call)
fun check() = ActionImpl(ActionType.Call)
fun raise(amount: UInt) = ActionImpl(ActionType.Raise, amount)
