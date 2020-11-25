package account

import FakeData.aAccount
import account.operation.Operation
import account.operation.OperationType.DEPOSIT
import account.operation.OperationType.WITHDRAWAL
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class AccountTest {

    @Test
    fun `should increment amount after deposit operation`() {
        // given
        val account = aAccount(amount = BigDecimal.ONE)
        val operation = Operation(operationType = DEPOSIT, amount = BigDecimal.ONE)

        // when
        val accountAfterDeposit = account.applyOperation(operation)

        // then
        assertTrue(accountAfterDeposit.amount == BigDecimal.valueOf(2L))
    }


    @Test
    fun `should decrement amount after withdrawal operation`() {
        // given
        val account = aAccount(amount = BigDecimal.ONE)
        val operation = Operation(operationType = WITHDRAWAL, amount = BigDecimal.ONE)
        // when
        val accountAfterWithdrawal = account.applyOperation(operation)

        // then
        assertTrue(accountAfterWithdrawal.amount == BigDecimal.ZERO)
    }

    @Test
    fun `should increment operations account after applying operation`() {
        // given
        val accountWithNoOperations = aAccount(operations = null)
        val accountWithOperations = aAccount()
        val operation = Operation(operationType = WITHDRAWAL, amount = BigDecimal.ONE)

        // when
        val accountAfterWithdrawal = accountWithNoOperations.applyOperation(operation)
        val accountAfterWithdrawal2 = accountWithOperations.applyOperation(operation)

        // then
        assertTrue(accountAfterWithdrawal.operations != null)
        assertTrue(accountAfterWithdrawal.operations!!.isNotEmpty())
        assertTrue(accountAfterWithdrawal2.operations!!.size > accountWithOperations.operations!!.size)
    }

    @Test
    fun `should construct empty history when operations does not exist`() {
        // given
        val accountWithNoOperations = aAccount(operations = null)

        // when
        val history = accountWithNoOperations.History()

        // then
        assertTrue(history.balance.deposits === BigDecimal.ZERO)
        assertTrue(history.balance.withdrawals === BigDecimal.ZERO)
        assertTrue(history.historyLines.isEmpty())
    }

    @Test
    fun `should generate balance from operations`() {
        // given
        val accountWithOperations1 = aAccount(operations = listOf(
                Operation(operationType = DEPOSIT, amount = BigDecimal.TEN),
                Operation(operationType = DEPOSIT, amount = BigDecimal.ONE),
                Operation(operationType = WITHDRAWAL, amount = "-15".toBigDecimal()),
                Operation(operationType = WITHDRAWAL, amount = "-100".toBigDecimal())
        ))

        val accountWithOperations2 = aAccount(operations = listOf(
                Operation(operationType = DEPOSIT, amount = BigDecimal.TEN),
                Operation(operationType = DEPOSIT, amount = BigDecimal.ONE))
        )

        val accountWithOperations3 = aAccount(operations = listOf(
                Operation(operationType = WITHDRAWAL, amount = "-15".toBigDecimal()),
                Operation(operationType = WITHDRAWAL, amount = "-100".toBigDecimal())
        ))

        // when
        val history1 = accountWithOperations1.History()
        val history2 = accountWithOperations2.History()
        val history3 = accountWithOperations3.History()

        // then
        assertTrue(history1.balance.deposits == "11".toBigDecimal())
        assertTrue(history1.balance.withdrawals == "-115".toBigDecimal())

         assertTrue(history2.balance.deposits == "11".toBigDecimal())
        assertTrue(history2.balance.withdrawals == BigDecimal.ZERO)

         assertTrue(history3.balance.deposits == BigDecimal.ZERO)
        assertTrue(history3.balance.withdrawals == "-115".toBigDecimal())


    }

    @Test
    fun `should generate history lines from operations`() {
        // given
        val accountWithOperations = aAccount(operations = listOf(
                Operation(operationType = DEPOSIT, amount = BigDecimal.TEN),
                Operation(operationType = DEPOSIT, amount = BigDecimal.ONE),
                Operation(operationType = WITHDRAWAL, amount = "-15".toBigDecimal()),
                Operation(operationType = WITHDRAWAL, amount = "-100".toBigDecimal())
        ))

        // when
        val history = accountWithOperations.History()

        // then
        assertTrue(accountWithOperations.operations!!.size == history.historyLines.size)
    }


}