package com.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: AccountType = AccountType.CASH,
    val balance: Double = 0.0,
    val currency: String = "USD",
    val icon: String = "wallet",
    val color: Int = 0xFF2196F3.toInt(),
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

enum class AccountType {
    CASH, BANK, CREDIT_CARD, WALLET, UPI, SAVINGS, INVESTMENT, LOAN, OTHER
}
