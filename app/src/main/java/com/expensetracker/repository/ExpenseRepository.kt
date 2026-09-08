package com.expensetracker.repository

import androidx.lifecycle.LiveData
import com.expensetracker.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val budgetDao: BudgetDao,
    private val accountDao: AccountDao,
    private val goalDao: GoalDao,
    private val debtDao: DebtDao
) {

    // Expense operations
    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllLiveData()
    val totalExpenses: LiveData<Double?> = expenseDao.getTotalLiveData()

    suspend fun insertExpense(expense: Expense) = withContext(Dispatchers.IO) {
        expenseDao.insert(expense)
    }

    suspend fun updateExpense(expense: Expense) = withContext(Dispatchers.IO) {
        expenseDao.update(expense)
    }

    suspend fun deleteExpense(expense: Expense) = withContext(Dispatchers.IO) {
        expenseDao.delete(expense)
    }

    suspend fun deleteExpenseById(id: Int) = withContext(Dispatchers.IO) {
        expenseDao.deleteById(id)
    }

    suspend fun getAllExpenses(): List<Expense> = withContext(Dispatchers.IO) {
        expenseDao.getAll()
    }

    suspend fun getTotalExpenses(): Double? = withContext(Dispatchers.IO) {
        expenseDao.getTotal()
    }

    suspend fun getTotalForPeriod(startTime: Long, endTime: Long): Double? = withContext(Dispatchers.IO) {
        expenseDao.getTotalForPeriod(startTime, endTime)
    }

    suspend fun getExpensesForPeriod(startTime: Long, endTime: Long): List<Expense> = withContext(Dispatchers.IO) {
        expenseDao.getExpensesForPeriod(startTime, endTime)
    }

    suspend fun getExpensesByCategory(category: String): List<Expense> = withContext(Dispatchers.IO) {
        expenseDao.getByCategory(category)
    }

    suspend fun getAllCategories(): List<String> = withContext(Dispatchers.IO) {
        expenseDao.getAllCategories()
    }

    suspend fun getCategoryTotals(): List<CategoryTotal> = withContext(Dispatchers.IO) {
        expenseDao.getCategoryTotals()
    }

    suspend fun searchExpenses(query: String): List<Expense> = withContext(Dispatchers.IO) {
        expenseDao.searchExpenses(query)
    }

    suspend fun getRecurringExpenses(): List<Expense> = withContext(Dispatchers.IO) {
        expenseDao.getRecurringExpenses()
    }

    suspend fun getMonthlyTotals(): List<MonthlyTotal> = withContext(Dispatchers.IO) {
        expenseDao.getMonthlyTotals()
    }

    suspend fun getExpenseCount(): Int = withContext(Dispatchers.IO) {
        expenseDao.getCount()
    }

    suspend fun getRecentExpenses(limit: Int): List<Expense> = withContext(Dispatchers.IO) {
        expenseDao.getRecentExpenses(limit)
    }

    // Budget operations
    fun getAllActiveBudgets(): Flow<List<Budget>> = budgetDao.getAllActiveBudgets()
    fun getOverallBudget(): Flow<Budget?> = budgetDao.getOverallBudget()
    fun getCategoryBudget(categoryId: String): Flow<Budget?> = budgetDao.getCategoryBudget(categoryId)

    suspend fun insertBudget(budget: Budget) = withContext(Dispatchers.IO) {
        budgetDao.insert(budget)
    }

    suspend fun updateBudget(budget: Budget) = withContext(Dispatchers.IO) {
        budgetDao.update(budget)
    }

    suspend fun deleteBudget(budget: Budget) = withContext(Dispatchers.IO) {
        budgetDao.delete(budget)
    }

    suspend fun deactivateBudget(id: Long) = withContext(Dispatchers.IO) {
        budgetDao.deactivate(id)
    }

    // Account operations
    fun getAllActiveAccounts(): Flow<List<Account>> = accountDao.getAllActiveAccounts()

    suspend fun insertAccount(account: Account) = withContext(Dispatchers.IO) {
        accountDao.insert(account)
    }

    suspend fun updateAccount(account: Account) = withContext(Dispatchers.IO) {
        accountDao.update(account)
    }

    suspend fun deleteAccount(account: Account) = withContext(Dispatchers.IO) {
        accountDao.delete(account)
    }

    suspend fun updateAccountBalance(id: Long, newBalance: Double) = withContext(Dispatchers.IO) {
        accountDao.updateBalance(id, newBalance)
    }

    suspend fun getTotalBalance(): Double? = withContext(Dispatchers.IO) {
        accountDao.getTotalBalance()
    }

    // Goal operations
    fun getAllActiveGoals(): Flow<List<Goal>> = goalDao.getAllActiveGoals()

    suspend fun insertGoal(goal: Goal) = withContext(Dispatchers.IO) {
        goalDao.insert(goal)
    }

    suspend fun updateGoal(goal: Goal) = withContext(Dispatchers.IO) {
        goalDao.update(goal)
    }

    suspend fun deleteGoal(goal: Goal) = withContext(Dispatchers.IO) {
        goalDao.delete(goal)
    }

    suspend fun updateGoalCurrentAmount(id: Long, amount: Double) = withContext(Dispatchers.IO) {
        goalDao.updateCurrentAmount(id, amount)
    }

    // Debt operations
    fun getAllActiveDebts(): Flow<List<Debt>> = debtDao.getAllActiveDebts()
    fun getDebtsByType(type: DebtType): Flow<List<Debt>> = debtDao.getDebtsByType(type)

    suspend fun insertDebt(debt: Debt) = withContext(Dispatchers.IO) {
        debtDao.insert(debt)
    }

    suspend fun updateDebt(debt: Debt) = withContext(Dispatchers.IO) {
        debtDao.update(debt)
    }

    suspend fun deleteDebt(debt: Debt) = withContext(Dispatchers.IO) {
        debtDao.delete(debt)
    }

    suspend fun settleDebt(id: Long) = withContext(Dispatchers.IO) {
        debtDao.settleDebt(id)
    }

    suspend fun updateDebtRemainingAmount(id: Long, amount: Double) = withContext(Dispatchers.IO) {
        debtDao.updateRemainingAmount(id, amount)
    }
}
