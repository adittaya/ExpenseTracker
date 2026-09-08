package com.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val deadline: Long? = null,
    val icon: String = "target",
    val color: Int = 0xFF4CAF50.toInt(),
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getProgress(): Float {
        return if (targetAmount > 0) (currentAmount / targetAmount).toFloat() else 0f
    }
    
    fun getProgressPercentage(): Int {
        return (getProgress() * 100).toInt().coerceIn(0, 100)
    }
}
