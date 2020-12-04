package agrfesta.k.cards.texasholdem.rules.gameplay

enum class ActionType {
    Call, Raise, Fold
}

interface Action {
    fun getAmount(): Int?
    fun getType(): ActionType
}

data class ActionImpl(private val type: ActionType, private val amount: Int? = null): Action {
    override fun getAmount() = amount
    override fun getType() = type
    override fun toString() = "$type${ if (amount!=null) " $amount" else "" }"
}
fun fold() = ActionImpl(ActionType.Fold)
fun call() = ActionImpl(ActionType.Call)
fun raise(amount: Int) = ActionImpl(ActionType.Raise, amount)
