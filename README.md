# Expense Tracker - Advanced Offline Expense Management

A fully functional, feature-rich offline expense tracking Android application built with Kotlin, Room Database, and Material Design.

## 🚀 Quick Start - Get Your APK

### Download Pre-built APK (via GitHub Actions)

1. **Push this code to your GitHub repository**
2. **Go to Actions tab** → Select "Build APK" workflow
3. **Download the artifact** → `ExpenseTracker-Debug-APK`

📖 **Detailed build instructions:** See [BUILD_APK_GUIDE.md](BUILD_APK_GUIDE.md)

### Build Locally

```bash
chmod +x gradlew
./gradlew assembleDebug
```

APK location: `app/build/outputs/apk/debug/app-debug.apk`

## ✨ Features

### Core Functionality
- 📊 **Dashboard Analytics** - Total expenses, monthly summaries, interactive charts
- ➕ **Add/Edit Expenses** - Amount, category, payment method, date, notes, tags
- 📝 **Expense List** - Search, filter, sort, long-press edit/delete
- 🔄 **Recurring Expenses** - Daily, weekly, monthly, yearly automation
- 💳 **Multiple Payment Methods** - Cash, Credit Card, Debit Card, UPI, etc.
- 🏷️ **Tag System** - Custom tags for better categorization
- 📈 **Visual Analytics** - Pie charts by category using MPAndroidChart

### Advanced Options
- 🔍 **Advanced Search** - Filter by amount, category, date range, notes
- 📅 **Period Filters** - Daily, weekly, monthly, yearly, custom range
- 🎨 **Category Colors** - 18 predefined categories with unique colors
- 💾 **Offline First** - All data stored locally with Room Database
- 📱 **Material Design** - Modern UI with smooth animations
- 🌙 **Pull to Refresh** - Update data with simple pull gesture

### Technical Highlights
- **Architecture**: MVVM with Repository pattern
- **Database**: Room (SQLite abstraction)
- **Async**: Kotlin Coroutines & Flow
- **UI**: ViewBinding, RecyclerView, Material Components
- **Charts**: MPAndroidChart for visualizations
- **Min SDK**: 24 (Android 7.0+)
- **Target SDK**: 34 (Android 14)

## 📋 Categories Supported

| Category | Color | Category | Color |
|----------|-------|----------|-------|
| Food & Dining | 🟠 | Shopping | 🔵 |
| Transportation | 🟢 | Entertainment | 🟣 |
| Bills & Utilities | 🔴 | Healthcare | 🩵 |
| Income | 🟢 | Education | 🟡 |
| Personal Care | 🩷 | Home & Garden | 🟤 |
| Gifts & Donations | 🟣 | Insurance | 🔵 |
| Savings & Investments | 🟢 | Travel | 🟠 |
| Pets | 🟤 | Other | ⚪ |

## 💳 Payment Methods

- Cash
- Credit Card
- Debit Card
- Net Banking
- UPI / Mobile Payment
- Wallet (Paytm, PhonePe, etc.)
- EMI
- Cheque
- Other

## 🛠️ Tech Stack

- **Language**: Kotlin 1.9.22
- **Min SDK**: 24 | **Target SDK**: 34
- **Database**: Room 2.6.1
- **Coroutines**: 1.7.3
- **Material Design**: 1.11.0
- **Charts**: MPAndroidChart v3.1.0
- **Build Tool**: Gradle 8.6
- **JDK**: 17

## 📁 Project Structure

```
app/src/main/java/com/expensetracker/
├── MainActivity.kt              # Dashboard with charts & list
├── AddExpenseActivity.kt        # Add/Edit expense form
├── data/
│   ├── Expense.kt               # Data entity
│   ├── ExpenseDao.kt            # Database access object
│   └── AppDatabase.kt           # Room database setup
├── repository/
│   └── ExpenseRepository.kt     # Data management layer
├── ui/
│   └── ExpenseAdapter.kt        # RecyclerView adapter
└── utils/
    ├── DateUtils.kt             # Date formatting helpers
    ├── CategoryColorUtils.kt    # Category color mapping
    └── PreferencesManager.kt    # Settings storage
```

## 🚀 GitHub Actions CI/CD

This project includes automated builds via GitHub Actions:

- ✅ Auto-builds on push to `main`/`master`
- ✅ Manual trigger via workflow_dispatch
- ✅ JDK 17 with Gradle caching
- ✅ APK verification & checksums
- ✅ 30-day artifact retention

See [`.github/workflows/android.yml`](.github/workflows/android.yml) for configuration.

## 📄 License

This project is open source and available for educational purposes.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## 📞 Support

For issues or questions:
- Check [BUILD_APK_GUIDE.md](BUILD_APK_GUIDE.md) for build help
- Review GitHub Actions logs for CI/CD issues
- Open an issue in this repository

---

**Built with ❤️ using Kotlin & Android Jetpack**