package com.expensetracker.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        const val KEY_CURRENCY = "currency"
        const val KEY_THEME = "theme"
        const val KEY_START_DAY_OF_MONTH = "start_day_of_month"
        const val KEY_SHOW_NOTIFICATIONS = "show_notifications"
        const val KEY_BACKUP_ENABLED = "backup_enabled"
        const val KEY_PASSCODE_ENABLED = "passcode_enabled"
        const val KEY_PASSCODE = "passcode"
        const val KEY_DEFAULT_CATEGORY = "default_category"
        const val KEY_DATE_FORMAT = "date_format"
    }

    var currency: String
        get() = prefs.getString(KEY_CURRENCY, "₹") ?: "₹"
        set(value) = prefs.edit().putString(KEY_CURRENCY, value).apply()

    var theme: String
        get() = prefs.getString(KEY_THEME, "light") ?: "light"
        set(value) = prefs.edit().putString(KEY_THEME, value).apply()

    var startDayOfMonth: Int
        get() = prefs.getInt(KEY_START_DAY_OF_MONTH, 1)
        set(value) = prefs.edit().putInt(KEY_START_DAY_OF_MONTH, value).apply()

    var showNotifications: Boolean
        get() = prefs.getBoolean(KEY_SHOW_NOTIFICATIONS, true)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_NOTIFICATIONS, value).apply()

    var backupEnabled: Boolean
        get() = prefs.getBoolean(KEY_BACKUP_ENABLED, false)
        set(value) = prefs.edit().putBoolean(KEY_BACKUP_ENABLED, value).apply()

    var passcodeEnabled: Boolean
        get() = prefs.getBoolean(KEY_PASSCODE_ENABLED, false)
        set(value) = prefs.edit().putBoolean(KEY_PASSCODE_ENABLED, value).apply()

    var passcode: String?
        get() = prefs.getString(KEY_PASSCODE, null)
        set(value) = prefs.edit().putString(KEY_PASSCODE, value).apply()

    var defaultCategory: String
        get() = prefs.getString(KEY_DEFAULT_CATEGORY, "Others") ?: "Others"
        set(value) = prefs.edit().putString(KEY_DEFAULT_CATEGORY, value).apply()

    var dateFormat: String
        get() = prefs.getString(KEY_DATE_FORMAT, "dd/MM/yyyy") ?: "dd/MM/yyyy"
        set(value) = prefs.edit().putString(KEY_DATE_FORMAT, value).apply()

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}

object Constants {
    val CATEGORIES = listOf(
        "Food & Dining",
        "Transportation",
        "Shopping",
        "Entertainment",
        "Bills & Utilities",
        "Healthcare",
        "Education",
        "Personal Care",
        "Home & Garden",
        "Insurance",
        "Savings",
        "Investment",
        "Gifts & Donations",
        "Travel",
        "Pets",
        "Subscriptions",
        "Others"
    )

    val PAYMENT_METHODS = listOf(
        "Cash",
        "Credit Card",
        "Debit Card",
        "UPI",
        "Net Banking",
        "Wallet",
        "Bank Transfer",
        "EMI",
        "Others"
    )

    val RECURRING_TYPES = listOf(
        "Daily",
        "Weekly",
        "Monthly",
        "Yearly"
    )

    val CURRENCIES = mapOf(
        "INR" to "₹",
        "USD" to "$",
        "EUR" to "€",
        "GBP" to "£",
        "JPY" to "¥",
        "AUD" to "A$",
        "CAD" to "C$",
        "CHF" to "Fr",
        "CNY" to "¥",
        "SGD" to "S$"
    )

    const val REQUEST_CODE_ADD_EXPENSE = 100
    const val REQUEST_CODE_EDIT_EXPENSE = 101
    const val REQUEST_CODE_SETTINGS = 102
    const val REQUEST_CODE_PASSCODE = 103
}
