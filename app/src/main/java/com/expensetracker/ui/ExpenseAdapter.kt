package com.expensetracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.expensetracker.R
import com.expensetracker.data.Expense
import com.expensetracker.utils.CategoryColorUtils
import com.expensetracker.utils.DateUtils
import java.text.NumberFormat
import java.util.*

class ExpenseAdapter(
    private var expenses: List<Expense>,
    private val currencySymbol: String = "₹",
    private val onItemClick: ((Expense) -> Unit)? = null,
    private val onEditClick: ((Expense) -> Unit)? = null,
    private val onDeleteClick: ((Expense) -> Unit)? = null
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private var showActions = false
    private var actionExpenseId: Int? = null

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewCategoryColor: View = itemView.findViewById(R.id.viewCategoryColor)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvNote: TextView = itemView.findViewById(R.id.tvNote)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvPaymentMethod: TextView = itemView.findViewById(R.id.tvPaymentMethod)
        val layoutActions: LinearLayout = itemView.findViewById(R.id.layoutActions)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
        val cardView: CardView = itemView as CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        val context = holder.itemView.context

        // Set category color
        val categoryColor = CategoryColorUtils.getCategoryColor(expense.category)
        holder.viewCategoryColor.setBackgroundColor(categoryColor)

        // Set category name
        holder.tvCategory.text = expense.category

        // Set amount with currency
        val amountFormatted = NumberFormat.getNumberInstance(Locale.getDefault()).format(expense.amount)
        holder.tvAmount.text = "$currencySymbol$amountFormatted"

        // Set note (or hide if empty)
        if (expense.note.isNotBlank()) {
            holder.tvNote.text = expense.note
            holder.tvNote.visibility = View.VISIBLE
        } else {
            holder.tvNote.visibility = View.GONE
        }

        // Set date
        holder.tvDate.text = DateUtils.formatDateTime(expense.timestamp)

        // Set payment method
        holder.tvPaymentMethod.text = expense.paymentMethod

        // Show/hide actions
        if (showActions && expense.id == actionExpenseId) {
            holder.layoutActions.visibility = View.VISIBLE
        } else {
            holder.layoutActions.visibility = View.GONE
        }

        // Click listeners
        holder.itemView.setOnClickListener {
            if (showActions && actionExpenseId != null) {
                showActions = false
                actionExpenseId = null
                notifyDataSetChanged()
            } else {
                onItemClick?.invoke(expense)
            }
        }

        holder.itemView.setOnLongClickListener {
            showActions = true
            actionExpenseId = expense.id
            notifyDataSetChanged()
            true
        }

        holder.btnEdit.setOnClickListener {
            onEditClick?.invoke(expense)
            showActions = false
            actionExpenseId = null
            notifyDataSetChanged()
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick?.invoke(expense)
            showActions = false
            actionExpenseId = null
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = expenses.size

    fun submitList(newExpenses: List<Expense>) {
        val diffCallback = ExpenseDiffCallback(expenses, newExpenses)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        expenses = newExpenses
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateExpense(updatedExpense: Expense) {
        val index = expenses.indexOfFirst { it.id == updatedExpense.id }
        if (index != -1) {
            expenses = expenses.toMutableList().apply {
                this[index] = updatedExpense
            }
            notifyItemChanged(index)
        }
    }

    fun removeExpense(expense: Expense) {
        val index = expenses.indexOfFirst { it.id == expense.id }
        if (index != -1) {
            expenses = expenses.toMutableList().apply {
                removeAt(index)
            }
            notifyItemRemoved(index)
        }
    }

    fun clearActions() {
        showActions = false
        actionExpenseId = null
        notifyDataSetChanged()
    }

    class ExpenseDiffCallback(
        private val oldList: List<Expense>,
        private val newList: List<Expense>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
