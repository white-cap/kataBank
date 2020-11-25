import account.Account
import account.AccountAmountOperationCommand
import account.Amount
import account.operation.Operation
import account.operation.OperationType
import java.math.BigDecimal
import java.util.*

internal object FakeData {

    fun aAccountAmountOperationCommand(amount: Amount = BigDecimal.valueOf(Random().nextLong()).abs())
            = AccountAmountOperationCommand(UUID.randomUUID().toString(), amount)

    fun aAccount(amount: Amount = BigDecimal.valueOf(Random().nextLong()).abs(),
                 operations : List<Operation>? = listOf(Operation(operationType = OperationType.DEPOSIT, amount = BigDecimal.TEN)))
            = Account(UUID.randomUUID().toString(), amount,operations)

}