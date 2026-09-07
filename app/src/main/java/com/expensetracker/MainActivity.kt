package com.expensetracker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.expensetracker.data.AppDatabase
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tv = TextView(this)
        setContentView(tv)

        val db = AppDatabase.get(this)

        CoroutineScope(Dispatchers.IO).launch {
            val total = db.dao().getTotal() ?: 0.0
            runOnUiThread {
                tv.text = "Total Expense: ₹$total"
            }
        }
    }
}
