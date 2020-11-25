package account

import FakeData.aAccount
import FakeData.aAccountAmountOperationCommand
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class AccountApplicationServiceTest {

    private val accountRepository = mockk<AccountRepository>(relaxed = true)
    private val accountApplicationService = AccountApplicationService(accountRepository)

    @Test
    fun `should not handle amount operation when account does not exist`() {
        // given
        val accountAmountOperationCommand = aAccountAmountOperationCommand()
        every { accountRepository.getAccountById(accountAmountOperationCommand.accountId) } returns null

        // when
        val result = accountApplicationService.makeAccountAmountOperation(accountAmountOperationCommand)

        // then
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    fun `should save account after a account amount operation`(){
        // given
        val command = aAccountAmountOperationCommand()
        every { accountRepository.getAccountById(command.accountId) } returns aAccount()

        // when
        accountApplicationService.makeAccountAmountOperation(command)

        // then
        verify(exactly = 1) { accountRepository.save(any()) }
    }
    @Test
    fun `should check if account exists or not`(){
        // given
        val accountIdOne = "1"
        val accountIdTwo = "2"
        every { accountRepository.getAccountById(accountIdOne) } returns null
        every { accountRepository.getAccountById(accountIdTwo) } returns aAccount()

        // when
        val resultOne = accountApplicationService.isAccountExist(accountIdOne)
        val resultTwo = accountApplicationService.isAccountExist(accountIdTwo)

        // then
        Assertions.assertFalse(resultOne)
        Assertions.assertTrue(resultTwo)
    }

    @Test
    fun `should not get history account when account does not exist`() {
        // given
        val accountId = "1234"
        every { accountRepository.getAccountById(accountId) } returns null

        // when
        val result = accountApplicationService.getAccountHistory(accountId)

        // then
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    fun `should get history for an account`(){
        // given
        val accountId = "1234"
        every { accountRepository.getAccountById(accountId) } returns aAccount()

        // when
        val result = accountApplicationService.getAccountHistory(accountId)

        // then
        Assertions.assertTrue(result.isSuccess)
        Assertions.assertTrue(result.getOrNull()!!.toString().isNotBlank())
    }

}

