package com.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val amount: Double,
    val period: BudgetPeriod = BudgetPeriod.MONTHLY,
    val categoryId: String? = null, // null for overall budget
    val startDate: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

enum class BudgetPeriod {
    DAILY, WEEKLY, MONTHLY, YEARLY
}
