package com.expensetracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActiveBudgets(): Flow<List<Budget>>
    
    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: Long): Budget?
    
    @Query("SELECT * FROM budgets WHERE categoryId IS NULL AND isActive = 1")
    fun getOverallBudget(): Flow<Budget?>
    
    @Query("SELECT * FROM budgets WHERE categoryId = :categoryId AND isActive = 1")
    fun getCategoryBudget(categoryId: String): Flow<Budget?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: Budget): Long
    
    @Update
    suspend fun update(budget: Budget)
    
    @Delete
    suspend fun delete(budget: Budget)
    
    @Query("UPDATE budgets SET isActive = 0 WHERE id = :id")
    suspend fun deactivate(id: Long)
}
