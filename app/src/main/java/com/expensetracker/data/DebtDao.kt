package com.expensetracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DebtDao {
    @Query("SELECT * FROM debts WHERE isSettled = 0 ORDER BY dueDate ASC")
    fun getAllActiveDebts(): Flow<List<Debt>>
    
    @Query("SELECT * FROM debts WHERE type = :type AND isSettled = 0")
    fun getDebtsByType(type: DebtType): Flow<List<Debt>>
    
    @Query("SELECT * FROM debts WHERE id = :id")
    suspend fun getDebtById(id: Long): Debt?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(debt: Debt): Long
    
    @Update
    suspend fun update(debt: Debt)
    
    @Delete
    suspend fun delete(debt: Debt)
    
    @Query("UPDATE debts SET remainingAmount = :amount WHERE id = :id")
    suspend fun updateRemainingAmount(id: Long, amount: Double)
    
    @Query("UPDATE debts SET isSettled = 1, settledDate = :date WHERE id = :id")
    suspend fun settleDebt(id: Long, date: Long = System.currentTimeMillis())
}
