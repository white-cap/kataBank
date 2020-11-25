package account

import account.operation.Operation
import account.operation.OperationType.DEPOSIT
import account.operation.OperationType.WITHDRAWAL
import java.math.BigDecimal


class AccountApplicationService(private val accountRepository: AccountRepository) {

    fun isAccountExist(accountId: AccountId): Boolean = accountRepository.getAccountById(accountId) != null

    fun makeAccountAmountOperation(command: AccountAmountOperationCommand): Result<Account> =
            withExistingAccount(command.accountId) {
                val operation = command.toOperation()
                val updatedAccount = it.applyOperation(operation)
                accountRepository.save(updatedAccount)
                updatedAccount
            }

    fun getAccountHistory(accountId: AccountId): Result<Account.History> =
            withExistingAccount(accountId) {
                it.History()
            }


    internal fun <T> withExistingAccount(accountId: AccountId, run: (Account) -> T): Result<T> = Result.runCatching {
        val account = accountRepository.getAccountById(accountId) ?: failNoExistingAccount()
        run(account)
    }
}

data class AccountAmountOperationCommand(val accountId: AccountId, val amount: Amount) {

    init {
        require(amount.abs() > BigDecimal.ONE) { "Amount should not be zero" }
    }

    fun toOperation(): Operation = when (amount.signum()) {
        1 -> Operation(operationType = DEPOSIT, amount = amount)
        -1 -> Operation(operationType = WITHDRAWAL, amount = amount)
        else -> throw IllegalArgumentException("Amount should not be zero")
    }
}

fun failNoExistingAccount(): Nothing = throw AssertionError("Account does not exist")
