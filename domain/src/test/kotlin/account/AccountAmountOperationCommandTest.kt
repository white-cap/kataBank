package account

import FakeData.aAccountAmountOperationCommand
import account.operation.OperationType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class AccountAmountOperationCommandTest{

    @Test
    fun `amount of account operation should not be handled a zero amount`(){
        // given
        val zeroAmount = BigDecimal.ZERO

        Assertions.assertThrows(IllegalArgumentException::class.java){ // then
            aAccountAmountOperationCommand(zeroAmount)  // when
        }
    }

    @Test
    fun `should transform account amount command to operation`(){
        // given
        val commandOne = aAccountAmountOperationCommand(BigDecimal.TEN)
        val commandTwo = aAccountAmountOperationCommand("-10".toBigDecimal())

        // when
        val operationOne = commandOne.toOperation()
        val operationTwo = commandTwo.toOperation()

        // then
        Assertions.assertTrue(operationOne.operationType === OperationType.DEPOSIT)
        Assertions.assertTrue(operationOne.amount == commandOne.amount)
        Assertions.assertTrue(operationTwo.operationType === OperationType.WITHDRAWAL)
        Assertions.assertTrue(operationTwo.amount == commandTwo.amount)
    }
}