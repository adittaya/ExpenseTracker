package com.expensetracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.expensetracker.data.AppDatabase
import com.expensetracker.data.Expense
import com.expensetracker.repository.ExpenseRepository
import com.expensetracker.utils.Constants
import com.expensetracker.utils.DateUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var repository: ExpenseRepository

    // UI Components
    private lateinit var etAmount: TextInputEditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var spinnerPaymentMethod: Spinner
    private lateinit var etDate: TextInputEditText
    private lateinit var etNote: TextInputEditText
    private lateinit var switchRecurring: SwitchMaterial
    private lateinit var layoutRecurringType: LinearLayout
    private lateinit var spinnerRecurringType: Spinner
    private lateinit var etTags: TextInputEditText
    private lateinit var btnSave: MaterialButton
    private lateinit var btnCancel: MaterialButton

    private var selectedDate: Long = System.currentTimeMillis()
    private var isEditMode = false
    private var expenseId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        // Initialize database and repository
        database = AppDatabase.get(this)
        repository = ExpenseRepository(database.dao())

        // Check if editing existing expense
        isEditMode = intent.hasExtra("EXPENSE_ID")
        expenseId = intent.getIntExtra("EXPENSE_ID", -1)

        // Initialize views
        initViews()
        setupSpinners()
        setupListeners()

        if (isEditMode) {
            title = "Edit Expense"
            populateFields()
        } else {
            title = "Add Expense"
        }
    }

    private fun initViews() {
        etAmount = findViewById(R.id.etAmount)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        spinnerPaymentMethod = findViewById(R.id.spinnerPaymentMethod)
        etDate = findViewById(R.id.etDate)
        etNote = findViewById(R.id.etNote)
        switchRecurring = findViewById(R.id.switchRecurring)
        layoutRecurringType = findViewById(R.id.layoutRecurringType)
        spinnerRecurringType = findViewById(R.id.spinnerRecurringType)
        etTags = findViewById(R.id.etTags)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        // Set default date
        updateDateDisplay()
    }

    private fun setupSpinners() {
        // Category spinner
        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Constants.CATEGORIES
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter

        // Payment method spinner
        val paymentAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Constants.PAYMENT_METHODS
        )
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPaymentMethod.adapter = paymentAdapter

        // Recurring type spinner
        val recurringAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Constants.RECURRING_TYPES
        )
        recurringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRecurringType.adapter = recurringAdapter
    }

    private fun setupListeners() {
        // Date picker
        etDate.setOnClickListener {
            showDatePicker()
        }

        // Recurring switch
        switchRecurring.setOnCheckedChangeListener { _, isChecked ->
            layoutRecurringType.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        // Save button
        btnSave.setOnClickListener {
            saveExpense()
        }

        // Cancel button
        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.time = Date(selectedDate)

        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val cal = Calendar.getInstance()
                cal.set(year, month, dayOfMonth)
                selectedDate = cal.timeInMillis
                updateDateDisplay()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateDisplay() {
        etDate.setText(DateUtils.formatDate(selectedDate, "dd/MM/yyyy"))
    }

    private fun populateFields() {
        lifecycleScope.launch {
            try {
                val amount = intent.getDoubleExtra("EXPENSE_AMOUNT", 0.0)
                val category = intent.getStringExtra("EXPENSE_CATEGORY") ?: ""
                val note = intent.getStringExtra("EXPENSE_NOTE") ?: ""
                val timestamp = intent.getLongExtra("EXPENSE_TIMESTAMP", System.currentTimeMillis())
                val paymentMethod = intent.getStringExtra("EXPENSE_PAYMENT_METHOD") ?: "Cash"
                val isRecurring = intent.getBooleanExtra("EXPENSE_IS_RECURRING", false)
                val recurringType = intent.getStringExtra("EXPENSE_RECURRING_TYPE")
                val tags = intent.getStringExtra("EXPENSE_TAGS")

                etAmount.setText(amount.toString())
                etNote.setText(note)
                etTags.setText(tags)
                selectedDate = timestamp
                updateDateDisplay()

                // Set category spinner
                val categoryIndex = Constants.CATEGORIES.indexOf(category)
                if (categoryIndex >= 0) {
                    spinnerCategory.setSelection(categoryIndex)
                }

                // Set payment method spinner
                val paymentIndex = Constants.PAYMENT_METHODS.indexOf(paymentMethod)
                if (paymentIndex >= 0) {
                    spinnerPaymentMethod.setSelection(paymentIndex)
                }

                // Set recurring
                switchRecurring.isChecked = isRecurring
                layoutRecurringType.visibility = if (isRecurring) View.VISIBLE else View.GONE

                // Set recurring type
                if (recurringType != null) {
                    val recurringIndex = Constants.RECURRING_TYPES.indexOf(recurringType)
                    if (recurringIndex >= 0) {
                        spinnerRecurringType.setSelection(recurringIndex)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@AddExpenseActivity, "Error loading expense", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveExpense() {
        // Validate amount
        val amountText = etAmount.text.toString().trim()
        if (amountText.isEmpty()) {
            etAmount.error = "Amount is required"
            etAmount.requestFocus()
            return
        }

        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            etAmount.error = "Enter a valid amount"
            etAmount.requestFocus()
            return
        }

        // Get other values
        val category = spinnerCategory.selectedItem.toString()
        val paymentMethod = spinnerPaymentMethod.selectedItem.toString()
        val note = etNote.text.toString().trim()
        val tags = etTags.text.toString().trim()
        val isRecurring = switchRecurring.isChecked
        val recurringType = if (isRecurring) spinnerRecurringType.selectedItem.toString() else null

        lifecycleScope.launch {
            try {
                if (isEditMode && expenseId != null) {
                    // Update existing expense
                    val updatedExpense = Expense(
                        id = expenseId!!,
                        amount = amount,
                        category = category,
                        note = note,
                        timestamp = selectedDate,
                        paymentMethod = paymentMethod,
                        isRecurring = isRecurring,
                        recurringType = recurringType,
                        tags = if (tags.isNotEmpty()) tags else null
                    )
                    withContext(Dispatchers.IO) {
                        repository.update(updatedExpense)
                    }
                    Toast.makeText(this@AddExpenseActivity, "Expense updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    // Insert new expense
                    val expense = Expense(
                        amount = amount,
                        category = category,
                        note = note,
                        timestamp = selectedDate,
                        paymentMethod = paymentMethod,
                        isRecurring = isRecurring,
                        recurringType = recurringType,
                        tags = if (tags.isNotEmpty()) tags else null
                    )
                    withContext(Dispatchers.IO) {
                        repository.insert(expense)
                    }
                    Toast.makeText(this@AddExpenseActivity, "Expense added successfully", Toast.LENGTH_SHORT).show()
                }

                setResult(RESULT_OK)
                finish()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@AddExpenseActivity, "Error saving expense: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
