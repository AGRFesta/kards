package agrfesta.k.cards.texasholdem.rules.gameplay

interface Action {
    fun getAmount(): Int?
}

class CallAction: Action {
    override fun getAmount(): Int? = null
    override fun toString(): String = "CALL"
}

class RaiseAction(private val amount: Int): Action {
    override fun getAmount(): Int? = amount
    override fun toString(): String = "RAISE $amount"
}

class FoldAction: Action {
    override fun getAmount(): Int? = null
    override fun toString(): String = "FOLD"
}

class CheckAction: Action {
    override fun getAmount(): Int? = null
    override fun toString(): String = "CHECK"
}