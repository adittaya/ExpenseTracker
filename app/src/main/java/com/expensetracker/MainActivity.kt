package com.expensetracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.expensetracker.data.AppDatabase
import com.expensetracker.data.CategoryTotal
import com.expensetracker.data.Expense
import com.expensetracker.repository.ExpenseRepository
import com.expensetracker.ui.ExpenseAdapter
import com.expensetracker.utils.Constants
import com.expensetracker.utils.DateUtils
import com.expensetracker.utils.PreferencesManager
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var repository: ExpenseRepository
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var expenseAdapter: ExpenseAdapter

    // UI Components
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recyclerViewExpenses: androidx.recyclerview.widget.RecyclerView
    private lateinit var emptyStateLayout: View
    private lateinit var fabAddExpense: FloatingActionButton
    private lateinit var tvTotalExpenses: TextView
    private lateinit var tvMonthExpenses: TextView
    private lateinit var etSearch: TextInputEditText
    private lateinit var pieChart: PieChart
    private lateinit var periodFilterContainer: LinearLayout
    private lateinit var tvViewAll: TextView

    private var currentPeriod = "THIS_MONTH"
    private val periods = listOf("TODAY", "YESTERDAY", "THIS_WEEK", "LAST_WEEK", "THIS_MONTH", "LAST_MONTH", "THIS_YEAR")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize database and repository
        database = AppDatabase.get(this)
        repository = ExpenseRepository(database.dao())
        preferencesManager = PreferencesManager(this)

        // Initialize UI components
        initViews()
        setupRecyclerView()
        setupPeriodFilters()
        setupListeners()

        // Load initial data
        loadExpenses()
    }

    private fun initViews() {
        swipeRefresh = findViewById(R.id.swipeRefresh)
        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        fabAddExpense = findViewById(R.id.fabAddExpense)
        tvTotalExpenses = findViewById(R.id.tvTotalExpenses)
        tvMonthExpenses = findViewById(R.id.tvMonthExpenses)
        etSearch = findViewById(R.id.etSearch)
        pieChart = findViewById(R.id.pieChart)
        periodFilterContainer = findViewById(R.id.periodFilterContainer)
        tvViewAll = findViewById(R.id.tvViewAll)
    }

    private fun setupRecyclerView() {
        expenseAdapter = ExpenseAdapter(
            expenses = emptyList(),
            currencySymbol = preferencesManager.currency,
            onItemClick = { expense ->
                // Show expense details (can be expanded later)
                Toast.makeText(this, "Amount: ${preferencesManager.currency}${expense.amount}", Toast.LENGTH_SHORT).show()
            },
            onEditClick = { expense ->
                openEditExpense(expense)
            },
            onDeleteClick = { expense ->
                deleteExpense(expense)
            }
        )

        recyclerViewExpenses.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = expenseAdapter
        }
    }

    private fun setupPeriodFilters() {
        periodFilterContainer.removeAllViews()

        periods.forEach { period ->
            val chip = Chip(this).apply {
                text = period.replace("_", " ")
                isCheckable = true
                isChecked = period == currentPeriod
                setChipBackgroundColorResource(
                    if (period == currentPeriod) R.color.primary_color else android.R.color.darker_gray
                )
                setTextColor(android.R.color.white)
            }

            chip.setOnClickListener {
                periods.forEachIndexed { index, p ->
                    val c = periodFilterContainer.getChildAt(index) as Chip
                    c.isChecked = p == period
                    c.setChipBackgroundColorResource(
                        if (p == period) R.color.primary_color else android.R.color.darker_gray
                    )
                }
                currentPeriod = period
                loadExpenses()
            }

            periodFilterContainer.addView(chip)
        }
    }

    private fun setupListeners() {
        fabAddExpense.setOnClickListener {
            openAddExpense()
        }

        swipeRefresh.setOnRefreshListener {
            loadExpenses()
        }

        tvViewAll.setOnClickListener {
            // Navigate to all expenses screen (can be added later)
            Toast.makeText(this, "View All clicked", Toast.LENGTH_SHORT).show()
        }

        // Search functionality
        etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && etSearch.text.isNullOrBlank()) {
                loadExpenses()
            }
        }
    }

    private fun loadExpenses() {
        lifecycleScope.launch {
            swipeRefresh.isRefreshing = true

            try {
                // Get all expenses
                val allExpenses = withContext(Dispatchers.IO) {
                    repository.getRecentExpenses(50)
                }

                // Get total expenses
                val total = withContext(Dispatchers.IO) {
                    repository.getTotal() ?: 0.0
                }

                // Get monthly expenses
                val (monthStart, monthEnd) = DateUtils.getPeriodString("THIS_MONTH")
                val monthTotal = withContext(Dispatchers.IO) {
                    repository.getTotalForPeriod(monthStart, monthEnd) ?: 0.0
                }

                // Update UI
                updateUI(allExpenses, total, monthTotal)

                // Load chart data
                loadChartData()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Error loading expenses: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun updateUI(expenses: List<Expense>, total: Double, monthTotal: Double) {
        val currency = preferencesManager.currency
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())

        tvTotalExpenses.text = "$currency${numberFormat.format(total)}"
        tvMonthExpenses.text = "$currency${numberFormat.format(monthTotal)}"

        // Update list
        expenseAdapter.submitList(expenses)

        // Show/hide empty state
        if (expenses.isEmpty()) {
            recyclerViewExpenses.visibility = View.GONE
            emptyStateLayout.visibility = View.VISIBLE
        } else {
            recyclerViewExpenses.visibility = View.VISIBLE
            emptyStateLayout.visibility = View.GONE
        }
    }

    private fun loadChartData() {
        lifecycleScope.launch {
            try {
                val categoryTotals = withContext(Dispatchers.IO) {
                    repository.getCategoryTotals()
                }

                if (categoryTotals.isNotEmpty()) {
                    setupPieChart(categoryTotals)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupPieChart(categoryTotals: List<CategoryTotal>) {
        val entries = categoryTotals.map {
            PieEntry(it.total.toFloat(), it.category)
        }

        val dataSet = PieDataSet(entries, "Expenses by Category").apply {
            colors = categoryTotals.map { getCategoryColor(it.category) }
            valueTextSize = 12f
            valueTextColor = android.graphics.Color.BLACK
            valueFormatter = PercentFormatter(pieChart)
            sliceSpace = 2f
            selectionShift = 5f
        }

        val data = PieData(dataSet)

        pieChart.apply {
            this.data = data
            description.isEnabled = false
            setDrawEntryLabels(false)
            isRotationEnabled = true
            setHoleRadius(40f)
            setTransparentCircleRadius(45f)
            setUsePercentValues(true)
            animateY(1000)
            invalidate()
        }
    }

    private fun getCategoryColor(category: String): Int {
        return com.expensetracker.utils.CategoryColorUtils.getCategoryColor(category)
    }

    private fun openAddExpense() {
        val intent = Intent(this, AddExpenseActivity::class.java)
        startActivityForResult(intent, Constants.REQUEST_CODE_ADD_EXPENSE)
    }

    private fun openEditExpense(expense: Expense) {
        val intent = Intent(this, AddExpenseActivity::class.java).apply {
            putExtra("EXPENSE_ID", expense.id)
            putExtra("EXPENSE_AMOUNT", expense.amount)
            putExtra("EXPENSE_CATEGORY", expense.category)
            putExtra("EXPENSE_NOTE", expense.note)
            putExtra("EXPENSE_TIMESTAMP", expense.timestamp)
            putExtra("EXPENSE_PAYMENT_METHOD", expense.paymentMethod)
            putExtra("EXPENSE_IS_RECURRING", expense.isRecurring)
            putExtra("EXPENSE_RECURRING_TYPE", expense.recurringType)
            putExtra("EXPENSE_TAGS", expense.tags)
        }
        startActivityForResult(intent, Constants.REQUEST_CODE_EDIT_EXPENSE)
    }

    private fun deleteExpense(expense: Expense) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.delete(expense)
                }
                Toast.makeText(this@MainActivity, "Expense deleted", Toast.LENGTH_SHORT).show()
                loadExpenses()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error deleting expense", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Constants.REQUEST_CODE_ADD_EXPENSE,
                Constants.REQUEST_CODE_EDIT_EXPENSE -> {
                    loadExpenses()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadExpenses()
    }
}
