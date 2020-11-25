package account

import account.operation.OperationEntity
import java.math.BigDecimal

data class AccountEntity(val accountId: AccountId, val amount: Amount,val operations :List<OperationEntity>?=null)

typealias AccountId = String
typealias Amount = BigDecimal
