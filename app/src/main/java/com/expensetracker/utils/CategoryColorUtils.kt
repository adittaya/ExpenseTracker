package com.expensetracker.utils

import android.content.Context
import android.graphics.Color
import java.util.*

object CategoryColorUtils {

    private val categoryColors = mapOf(
        "Food & Dining" to "#FF6B6B",
        "Transportation" to "#4ECDC4",
        "Shopping" to "#FFE66D",
        "Entertainment" to "#95E1D3",
        "Bills & Utilities" to "#F38181",
        "Healthcare" to "#AA96DA",
        "Education" to "#FCBAD3",
        "Personal Care" to "#A8D8EA",
        "Home & Garden" to "#87CEEB",
        "Insurance" to "#DDA0DD",
        "Savings" to "#98FB98",
        "Investment" to "#FFD700",
        "Gifts & Donations" to "#FFA07A",
        "Travel" to "#20B2AA",
        "Pets" to "#DEB887",
        "Subscriptions" to "#BA55D3",
        "Others" to "#D3D3D3"
    )

    fun getCategoryColor(category: String): Int {
        val colorHex = categoryColors[category] ?: generateColorForCategory(category)
        return Color.parseColor(colorHex)
    }

    fun getCategoryColorWithDefault(category: String, defaultIndex: Int): Int {
        val predefinedColor = categoryColors[category]
        return if (predefinedColor != null) {
            Color.parseColor(predefinedColor)
        } else {
            getDefaultColor(defaultIndex)
        }
    }

    private fun generateColorForCategory(category: String): String {
        // Generate a consistent color based on category name hash
        val hash = category.hashCode()
        val hue = (hash % 360).toFloat()
        return String.format("#%06X", (Color.HSVToColor(floatArrayOf(hue, 0.7f, 0.9f)) and 0xFFFFFF))
    }

    private fun getDefaultColor(index: Int): Int {
        val colors = listOf(
            Color.parseColor("#FF6B6B"),
            Color.parseColor("#4ECDC4"),
            Color.parseColor("#FFE66D"),
            Color.parseColor("#95E1D3"),
            Color.parseColor("#F38181"),
            Color.parseColor("#AA96DA"),
            Color.parseColor("#FCBAD3"),
            Color.parseColor("#A8D8EA"),
            Color.parseColor("#87CEEB"),
            Color.parseColor("#DDA0DD")
        )
        return colors[index % colors.size]
    }

    fun getAllCategoryColors(): Map<String, Int> {
        return categoryColors.mapValues { Color.parseColor(it.value) }
    }

    fun getRandomColor(): Int {
        val random = Random()
        return Color.argb(
            255,
            random.nextInt(200),
            random.nextInt(200),
            random.nextInt(200)
        )
    }
}
