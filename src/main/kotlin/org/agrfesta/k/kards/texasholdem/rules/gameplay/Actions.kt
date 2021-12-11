package org.agrfesta.k.kards.texasholdem.rules.gameplay

enum class ActionType {
    Call, Raise, Fold
}

interface Action {
    val amount: Int?
    val type: ActionType
}

data class ActionImpl(
    override val type: ActionType,
    override val amount: Int? = null): Action {
    override fun toString() = "$type${ if (amount!=null) " $amount" else "" }"
}
fun fold() = ActionImpl(ActionType.Fold)
fun call() = ActionImpl(ActionType.Call)
fun raise(amount: Int) = ActionImpl(ActionType.Raise, amount)
