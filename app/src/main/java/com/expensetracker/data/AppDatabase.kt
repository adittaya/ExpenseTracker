package com.expensetracker.data

import android.content.Context
import androidx.room.*

@Database(entities = [Expense::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): ExpenseDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context, AppDatabase::class.java, "db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
