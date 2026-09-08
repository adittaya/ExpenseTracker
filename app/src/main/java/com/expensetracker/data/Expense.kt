package com.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(indices = [Index(value = ["category"]), Index(value = ["timestamp"])])
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val category: String,
    val note: String,
    val timestamp: Long = System.currentTimeMillis(),
    val paymentMethod: String = "Cash",
    val isRecurring: Boolean = false,
    val recurringType: String? = null, // daily, weekly, monthly
    val tags: String? = null // comma separated tags
)
