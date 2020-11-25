package account

interface AccountRepository {
    fun getAccountById(accountId: AccountId) : Account?
    fun save(account: Account)
}