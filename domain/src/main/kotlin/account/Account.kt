package account

import account.operation.Operation
import account.operation.OperationType.DEPOSIT
import account.operation.OperationType.WITHDRAWAL
import java.math.BigDecimal

data class Account(val accountId: AccountId, val amount: Amount, val operations: List<Operation>? = null) {

    fun applyOperation(operation: Operation): Account =
            operation
                    .operationType
                    .operationTypeHandler(
                            this.copy(operations = this.operations?.let { listOf(operation) + this.operations }
                                    ?: listOf(operation)),
                            operation.amount
                    )

    inner class History(account: Account = this@Account) {
        val balance: Balance
        val historyLines: List<String>

        init {
            if (account.operations != null) {
                balance = generateBalance(account)
                historyLines = generateHistoryLines(account)
            } else {
                balance = Balance(BigDecimal.ZERO, BigDecimal.ZERO)
                historyLines = listOf()
            }
        }

        private fun generateHistoryLines(account: Account): List<String> {
            return account.operations!!.map {
                "- Operation label : ${it.operationType.name.toLowerCase()}; " +
                        "DateTime : ${it.dateTime}; " +
                        "Amount : ${it.amount}"
            }.toList()
        }

        private fun generateBalance(account: Account): Balance =
                Balance(
                        account.operations!!.filter { it.operationType === DEPOSIT }.map(Operation::amount).fold(BigDecimal.ZERO, { left, right -> left + right }),
                        account.operations.filter { it.operationType === WITHDRAWAL }.map(Operation::amount).fold(BigDecimal.ZERO, { left, right -> left + right })
                )


    }
}

data class Balance(val deposits: Amount, val withdrawals: Amount)
typealias AccountId = String
