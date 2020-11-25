import account.AccountApplicationService
import account.AccountEntity
import account.AccountAmountOperationCommand
import account.AccountPersistence
import account.operation.OperationEntity
import account.operation.OperationType
import account.operation.OperationType.DEPOSIT
import account.operation.OperationType.WITHDRAWAL
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.reflect.KClass
import kotlin.system.exitProcess

fun main() {
    Application(Context.getBean(AccountApplicationService::class))
            .welcome()
            .login()
            .navigate()
}

class Application(private val accountApplicationService: AccountApplicationService) {

    private lateinit var accountId: String

    fun welcome() = println(WELCOME).let { this }

    fun login(): Application {
        print(LOGIN)
        accountId = scan()
        return when (accountApplicationService.isAccountExist(accountId)) {
            true -> this
            else -> println("This account does not exist").run { login() }
        }
    }

    fun navigate() {
        println(MENU)
        when (scan()) {
            "1" -> doOperation(DEPOSIT)
            "2" -> doOperation(WITHDRAWAL)
            "3" -> history()
            "4" -> exitProcess(0)
            else -> exitProcess(0)
        }
    }

    private fun history() {
        val resultAccountHistory = accountApplicationService.getAccountHistory(accountId)
        if (resultAccountHistory.isFailure) {
            println("Error occurred when getting history account because ${resultAccountHistory.exceptionOrNull()!!.message}")
            navigate()
        }
        val accountHistory = resultAccountHistory.getOrNull()!!

        if(accountHistory.historyLines.isEmpty()){
            noHistory()
        }
        println("History of account ID : $accountId")
        println("_______________________________")
        println("Operations:")
        accountHistory.historyLines.forEach(System.out::println)
        println("\nAccount Balance : Deposits: ${accountHistory.balance.deposits} | Withdrawals : ${accountHistory.balance.withdrawals}")
        navigate()
    }


    private fun doOperation(operationType: OperationType, operationWording: String = DEPOSE_OR_WITHDRAW.format(operationType.name.toLowerCase())) {
        println(operationWording)
        scan().let {
            try {
                val amount = it.toBigDecimal().let { if (operationType === DEPOSIT) it.abs() else it.abs().multiply("-1".toBigDecimal()) }
                val accountAmountOperationCommand = AccountAmountOperationCommand(accountId, amount)
                accountApplicationService.makeAccountAmountOperation(accountAmountOperationCommand).run {
                    when (isSuccess) {
                        true -> println("${operationType.name.toLowerCase()} successfully !").run { navigate() }
                        false -> println("${operationType.name.toLowerCase()} error: ${exceptionOrNull()!!.message}")
                    }
                }
            } catch (e: NumberFormatException) {
                if (it == RETURN) navigate()
                doOperation(operationType, INCORRECT_AMOUNT.format(operationType.name.toLowerCase()))
            } catch (e: Exception) {
                println("${operationType.name.toLowerCase()} error: ${e.message}")
                doOperation(operationType)
            }
        }
    }


    companion object Data {

        const val WELCOME = "Welcome to your bank application"
        const val LOGIN = "Enter your bank account ID:"
        const val MENU = "Menu:\n1: Deposit\n2: Withdrawal\n3: History\n4: Exit"
        const val DEPOSE_OR_WITHDRAW = "%s an amount:"
        const val RETURN = "return"
        const val INCORRECT_AMOUNT = "Incorrect %s amount try again or type return to go back to menu:"

        init {
            AccountPersistence.memory.putAll(
                    mapOf(
                            "1" to AccountEntity(accountId = "1", amount = BigDecimal.ZERO),
                            "2" to AccountEntity(accountId = "2", amount = BigDecimal.TEN, operations = listOf(OperationEntity("DEPOSIT", LocalDateTime.now(), BigDecimal.TEN))),
                            "3" to AccountEntity(accountId = "2", amount = "-100".toBigDecimal(), operations = listOf(
                                    OperationEntity("DEPOSIT", LocalDateTime.now(), "100".toBigDecimal()),
                                    OperationEntity("WITHDRAWAL", LocalDateTime.now(), "-200".toBigDecimal())

                            ))
                    )
            )
        }
    }

    private fun scan() = readLine()!!.trim()

    private fun noHistory() {
        println("No history for account ID : $accountId")
        navigate()
    }
}

object Context {
    private val beans = mapOf(AccountApplicationService::class.simpleName!! to AccountApplicationService(AccountPersistence()))

    fun <T : Any> getBean(clazz: KClass<T>): T = beans[clazz.simpleName!!] as T
}