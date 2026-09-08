package com.expensetracker.utils

/**
 * Application-wide constants
 */
object Constants {

    // Default Categories with icons and colors
    val DEFAULT_EXPENSE_CATEGORIES = listOf(
        CategoryData("Food & Dining", "restaurant", 0xFFFF5252.toInt()),
        CategoryData("Transportation", "directions_car", 0xFFFF9800.toInt()),
        CategoryData("Shopping", "shopping_cart", 0xFFE91E63.toInt()),
        CategoryData("Entertainment", "movie", 0xFF9C27B0.toInt()),
        CategoryData("Bills & Utilities", "receipt", 0xFF3F51B5.toInt()),
        CategoryData("Healthcare", "local_hospital", 0xFF00BCD4.toInt()),
        CategoryData("Education", "school", 0xFF4CAF50.toInt()),
        CategoryData("Personal Care", "spa", 0xFF8BC34A.toInt()),
        CategoryData("Home & Garden", "home", 0xFFCDDC39.toInt()),
        CategoryData("Insurance", "security", 0xFFFFC107.toInt()),
        CategoryData("Savings", "savings", 0xFFFF9800.toInt()),
        CategoryData("Investments", "trending_up", 0xFF4CAF50.toInt()),
        CategoryData("Gifts & Donations", "card_giftcard", 0xFFE91E63.toInt()),
        CategoryData("Travel", "flight", 0xFF03A9F4.toInt()),
        CategoryData("Pets", "pets", 0xFF795548.toInt()),
        CategoryData("Sports & Fitness", "fitness_center", 0xFF009688.toInt()),
        CategoryData("Subscriptions", "repeat", 0xFF607D8B.toInt()),
        CategoryData("Other", "more_horiz", 0xFF9E9E9E.toInt())
    )

    val DEFAULT_INCOME_CATEGORIES = listOf(
        CategoryData("Salary", "work", 0xFF4CAF50.toInt()),
        CategoryData("Business", "store", 0xFF8BC34A.toInt()),
        CategoryData("Investments", "trending_up", 0xFF03A9F4.toInt()),
        CategoryData("Gifts", "card_giftcard", 0xFFE91E63.toInt()),
        CategoryData("Refunds", "restore", 0xFFFF9800.toInt()),
        CategoryData("Other Income", "add_circle", 0xFF9E9E9E.toInt())
    )

    // Payment Methods
    val PAYMENT_METHODS = listOf(
        "Cash",
        "Credit Card",
        "Debit Card",
        "Bank Transfer",
        "UPI",
        "Wallet",
        "Net Banking",
        "EMI",
        "Other"
    )

    // Recurring Types
    val RECURRING_TYPES = listOf(
        "One-time",
        "Daily",
        "Weekly",
        "Bi-weekly",
        "Monthly",
        "Quarterly",
        "Yearly"
    )

    // Date Formats
    const val DISPLAY_DATE_FORMAT = "MMM dd, yyyy"
    const val DISPLAY_TIME_FORMAT = "hh:mm a"
    const val DISPLAY_DATETIME_FORMAT = "MMM dd, yyyy hh:mm a"
    const val DATABASE_DATE_FORMAT = "yyyy-MM-dd"
    const val DATABASE_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"

    // Preferences Keys
    const val PREF_THEME = "theme"
    const val PREF_CURRENCY = "currency"
    const val PREF_CURRENCY_SYMBOL = "currency_symbol"
    const val PREF_START_OF_WEEK = "start_of_week"
    const val PREF_APP_LOCK = "app_lock_enabled"
    const val PREF_BIOMETRIC = "biometric_enabled"

    // Database
    const val DATABASE_NAME = "expense_tracker_db"
    const val DATABASE_VERSION = 3

    // Limits
    const val MAX_NOTE_LENGTH = 500
    const val MAX_TAGS = 10
    const val DEFAULT_RECENT_LIMIT = 10
    const val MAX_RECENT_LIMIT = 50

    // Budget Periods
    const val BUDGET_PERIOD_DAILY = "daily"
    const val BUDGET_PERIOD_WEEKLY = "weekly"
    const val BUDGET_PERIOD_MONTHLY = "monthly"
    const val BUDGET_PERIOD_YEARLY = "yearly"

    // Account Types
    const val ACCOUNT_TYPE_CASH = "cash"
    const val ACCOUNT_TYPE_BANK = "bank"
    const val ACCOUNT_TYPE_CREDIT_CARD = "credit_card"
    const val ACCOUNT_TYPE_WALLET = "wallet"
    const val ACCOUNT_TYPE_SAVINGS = "savings"
    const val ACCOUNT_TYPE_INVESTMENT = "investment"
}

data class CategoryData(
    val name: String,
    val icon: String,
    val color: Int
)
