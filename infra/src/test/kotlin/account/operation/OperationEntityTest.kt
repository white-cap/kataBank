package account.operation

import account.operation.OperationType.DEPOSIT
import account.operation.OperationType.WITHDRAWAL
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.math.BigDecimal

internal class OperationEntityTest {

    @Test
    fun `should transform operations to operations entity`() {
        // given
        val operations =listOf(
                Operation(operationType = DEPOSIT, amount = BigDecimal.TEN),
                Operation(operationType = WITHDRAWAL, amount = "-100".toBigDecimal())
        )
        // when
        val operationEntities = operations.toOperationEntities()
        // then

        Assertions.assertAll(
                Executable { Assertions.assertEquals(operationEntities[0].amount, BigDecimal.TEN) },
                Executable { Assertions.assertEquals(operationEntities[0].operationType, "DEPOSIT") },
                Executable { Assertions.assertEquals(operationEntities[1].operationType, "WITHDRAWAL") },
                Executable { Assertions.assertEquals(operationEntities[1].amount, "-100".toBigDecimal()) }
        )
    }

    @Test
    fun `should transform operations entity to operations`() {
        // given
        val operationEntities =listOf(
                Operation(operationType = DEPOSIT, amount = BigDecimal.TEN),
                Operation(operationType = WITHDRAWAL, amount = "-100".toBigDecimal())
        ).toOperationEntities()

        // when
        val operations = operationEntities.toOperations()

        // then
        Assertions.assertAll(
                Executable { Assertions.assertEquals(operations[0].amount, BigDecimal.TEN) },
                Executable { Assertions.assertEquals(operations[0].operationType, DEPOSIT) },
                Executable { Assertions.assertEquals(operations[1].operationType,WITHDRAWAL) },
                Executable { Assertions.assertEquals(operations[1].amount, "-100".toBigDecimal()) }
        )
    }
}