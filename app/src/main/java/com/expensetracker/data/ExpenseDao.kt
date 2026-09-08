package com.expensetracker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.Date

@Dao
interface ExpenseDao {
    @Insert suspend fun insert(expense: Expense)
    @Update suspend fun update(expense: Expense)
    @Delete suspend fun delete(expense: Expense)
    
    @Query("SELECT * FROM Expense ORDER BY timestamp DESC")
    suspend fun getAll(): List<Expense>
    
    @Query("SELECT * FROM Expense ORDER BY timestamp DESC")
    fun getAllLiveData(): LiveData<List<Expense>>
    
    @Query("SELECT SUM(amount) FROM Expense")
    suspend fun getTotal(): Double?
    
    @Query("SELECT SUM(amount) FROM Expense")
    fun getTotalLiveData(): LiveData<Double?>
    
    @Query("SELECT SUM(amount) FROM Expense WHERE timestamp >= :startTime AND timestamp <= :endTime")
    suspend fun getTotalForPeriod(startTime: Long, endTime: Long): Double?
    
    @Query("SELECT * FROM Expense WHERE category = :category ORDER BY timestamp DESC")
    suspend fun getByCategory(category: String): List<Expense>
    
    @Query("SELECT * FROM Expense WHERE timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp DESC")
    suspend fun getExpensesForPeriod(startTime: Long, endTime: Long): List<Expense>
    
    @Query("SELECT DISTINCT category FROM Expense ORDER BY category ASC")
    suspend fun getAllCategories(): List<String>
    
    @Query("SELECT category, SUM(amount) as total FROM Expense GROUP BY category ORDER BY total DESC")
    suspend fun getCategoryTotals(): List<CategoryTotal>
    
    @Query("SELECT * FROM Expense WHERE note LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    suspend fun searchExpenses(query: String): List<Expense>
    
    @Query("DELETE FROM Expense WHERE id = :id")
    suspend fun deleteById(id: Int)
    
    @Query("SELECT * FROM Expense WHERE isRecurring = 1")
    suspend fun getRecurringExpenses(): List<Expense>
    
    @Query("SELECT strftime('%Y-%m', timestamp/1000) as month, SUM(amount) as total FROM Expense GROUP BY month ORDER BY month DESC LIMIT 12")
    suspend fun getMonthlyTotals(): List<MonthlyTotal>
    
    @Query("SELECT COUNT(*) FROM Expense")
    suspend fun getCount(): Int
    
    @Query("SELECT * FROM Expense ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentExpenses(limit: Int): List<Expense>
}

data class CategoryTotal(
    val category: String,
    val total: Double
)

data class MonthlyTotal(
    val month: String,
    val total: Double
)
