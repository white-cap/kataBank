package account

import account.operation.toOperations
import account.operation.toOperationEntities


class AccountPersistence : AccountRepository {

    override fun getAccountById(accountId: AccountId): Account? =
            memory[accountId]?.let {
                Account(
                        accountId = it.accountId,
                        amount = it.amount,
                        operations = it.operations?.toOperations()
                )
            }


    override fun save(account: Account) =
            account.let {
                memory[account.accountId] = AccountEntity(
                        accountId = it.accountId,
                        amount = it.amount,
                        operations = it.operations?.toOperationEntities()
                )
            }


    companion object InMemory {
        val memory: MutableMap<String, AccountEntity> = mutableMapOf()
    }
}