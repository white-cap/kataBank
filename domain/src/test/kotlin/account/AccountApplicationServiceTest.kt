package account

import FakeData.aAccount
import FakeData.aAccountAmountOperationCommand
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class AccountApplicationServiceTest {

    private val accountRepository = mockk<AccountRepository>(relaxed = true)
    private val accountApplicationService = spyk(AccountApplicationService(accountRepository))

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
    fun `should update account after a account amount operation`() {
        // given
        val command = aAccountAmountOperationCommand()
        val account = spyk(aAccount())
        every { accountRepository.getAccountById(command.accountId) } returns account

        // when
        val updatedAccount = accountApplicationService.makeAccountAmountOperation(command).getOrNull()!!

        // then
        verify(exactly = 1) { accountRepository.save(any()) }
        verify(exactly = 1) { account.applyOperation(any()) }
        Assertions.assertTrue(updatedAccount !== account) // immutability test
        Assertions.assertTrue(updatedAccount.amount != account.amount)
        Assertions.assertTrue(updatedAccount.operations!!.size != account.operations!!.size)
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

