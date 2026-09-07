package com.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val category: String,
    val note: String,
    val timestamp: Long = System.currentTimeMillis()
)
