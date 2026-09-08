package com.expensetracker.repository

import androidx.lifecycle.LiveData
import com.expensetracker.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpenseRepository(private val dao: ExpenseDao) {

    val allExpenses: LiveData<List<Expense>> = dao.getAllLiveData()
    val totalExpenses: LiveData<Double?> = dao.getTotalLiveData()

    suspend fun insert(expense: Expense) = withContext(Dispatchers.IO) {
        dao.insert(expense)
    }

    suspend fun update(expense: Expense) = withContext(Dispatchers.IO) {
        dao.update(expense)
    }

    suspend fun delete(expense: Expense) = withContext(Dispatchers.IO) {
        dao.delete(expense)
    }

    suspend fun deleteById(id: Int) = withContext(Dispatchers.IO) {
        dao.deleteById(id)
    }

    suspend fun getAll(): List<Expense> = withContext(Dispatchers.IO) {
        dao.getAll()
    }

    suspend fun getTotal(): Double? = withContext(Dispatchers.IO) {
        dao.getTotal()
    }

    suspend fun getTotalForPeriod(startTime: Long, endTime: Long): Double? = withContext(Dispatchers.IO) {
        dao.getTotalForPeriod(startTime, endTime)
    }

    suspend fun getExpensesForPeriod(startTime: Long, endTime: Long): List<Expense> = withContext(Dispatchers.IO) {
        dao.getExpensesForPeriod(startTime, endTime)
    }

    suspend fun getByCategory(category: String): List<Expense> = withContext(Dispatchers.IO) {
        dao.getByCategory(category)
    }

    suspend fun getAllCategories(): List<String> = withContext(Dispatchers.IO) {
        dao.getAllCategories()
    }

    suspend fun getCategoryTotals(): List<CategoryTotal> = withContext(Dispatchers.IO) {
        dao.getCategoryTotals()
    }

    suspend fun searchExpenses(query: String): List<Expense> = withContext(Dispatchers.IO) {
        dao.searchExpenses(query)
    }

    suspend fun getRecurringExpenses(): List<Expense> = withContext(Dispatchers.IO) {
        dao.getRecurringExpenses()
    }

    suspend fun getMonthlyTotals(): List<MonthlyTotal> = withContext(Dispatchers.IO) {
        dao.getMonthlyTotals()
    }

    suspend fun getCount(): Int = withContext(Dispatchers.IO) {
        dao.getCount()
    }

    suspend fun getRecentExpenses(limit: Int): List<Expense> = withContext(Dispatchers.IO) {
        dao.getRecentExpenses(limit)
    }
}
