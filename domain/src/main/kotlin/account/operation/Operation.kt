package account.operation

import account.Account
import account.Amount
import java.time.LocalDateTime

data class Operation(val operationType: OperationType, val dateTime: LocalDateTime = LocalDateTime.now(), val amount: Amount)

enum class OperationType(val operationTypeHandler: OperationTypeHandler) {
    DEPOSIT({ account: Account, depositAmount : Amount -> account.copy(amount = account.amount + depositAmount)}),
    WITHDRAWAL({ account: Account, withdrawalAmount : Amount -> account.copy(amount = account.amount - withdrawalAmount)})
}

typealias OperationTypeHandler = (Account, Amount) -> Account