package com.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debts")
data class Debt(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personName: String,
    val amount: Double,
    val remainingAmount: Double = amount,
    val type: DebtType = DebtType.BORROWED,
    val description: String = "",
    val date: Long = System.currentTimeMillis(),
    val dueDate: Long? = null,
    val isSettled: Boolean = false,
    val settledDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getProgress(): Float {
        return if (amount > 0) ((amount - remainingAmount) / amount).toFloat() else 0f
    }
}

enum class DebtType {
    BORROWED, LENT
}
