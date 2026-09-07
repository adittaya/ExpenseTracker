package com.expensetracker.data

import androidx.room.*

@Dao
interface ExpenseDao {
    @Insert suspend fun insert(expense: Expense)
    @Query("SELECT * FROM Expense ORDER BY timestamp DESC")
    suspend fun getAll(): List<Expense>
    @Query("SELECT SUM(amount) FROM Expense")
    suspend fun getTotal(): Double?
}
