package agrfesta.k.cards.texasholdem.rules.gameplay

enum class ActionType {
    Call, Raise, Fold
}

interface Action {
    fun getAmount(): Int?
    fun getType(): ActionType
}

class CallAction(private val amount: Int? = null): Action {
    override fun getAmount(): Int? = amount
    override fun getType() = ActionType.Call
    override fun toString(): String = "CALL"
}

class RaiseAction(private val amount: Int): Action {
    override fun getAmount(): Int? = amount
    override fun getType() = ActionType.Raise
    override fun toString(): String = "RAISE $amount"
}

class FoldAction: Action {
    override fun getAmount(): Int? = null
    override fun getType() = ActionType.Fold
    override fun toString(): String = "FOLD"
}

//class CheckAction: Action {
//    override fun getAmount(): Int? = null
//    override fun toString(): String = "CHECK"
//}