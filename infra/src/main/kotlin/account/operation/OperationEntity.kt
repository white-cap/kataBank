package account.operation

import java.math.BigDecimal
import java.time.LocalDateTime

data class OperationEntity(val operationType: OperationTypeName, val dateTime: LocalDateTime, val amount: Amount)

fun List<OperationEntity>.toOperations(): List<Operation> =
        this.map { Operation(
                operationType =  OperationType.valueOf(it.operationType),
                dateTime = it.dateTime,
                amount = it.amount
        ) }.toList()

fun List<Operation>.toOperationEntities(): List<OperationEntity> =
        this.map { OperationEntity(
                operationType = it.operationType.name,
                dateTime = it.dateTime,
                amount = it.amount
        ) }

typealias OperationTypeName = String
typealias Amount = BigDecimal