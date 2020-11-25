package account

import account.FakeData.aAccount
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal


internal class AccountPersistenceTest {

    private val accountPersistence = AccountPersistence()

    @BeforeEach
    fun init() {
        AccountPersistence.memory.putAll(mapOf("1" to AccountEntity(accountId = "1", amount = BigDecimal.ZERO)))
    }

    @Test
    fun `should get an account by ID`() {
        // given
        val accountId = "1"

        // when
        val account = accountPersistence.getAccountById(accountId)

        // then
        Assertions.assertTrue(account != null)
    }

    @Test
    fun `should save an account with operation`() {
        // given
        val account = aAccount()

        // when
        accountPersistence.save(account)

        // then
        Assertions.assertTrue(AccountPersistence.memory.contains(account.accountId))
        Assertions.assertTrue(AccountPersistence.memory[account.accountId]!!.operations!! != null)
    }


}