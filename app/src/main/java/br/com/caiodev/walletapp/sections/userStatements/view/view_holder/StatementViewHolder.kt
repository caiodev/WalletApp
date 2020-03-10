package br.com.caiodev.walletapp.sections.userStatements.view.view_holder

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.caiodev.walletapp.R
import br.com.caiodev.walletapp.sections.userStatements.model.viewTypes.Statements
import br.com.caiodev.walletapp.utils.text.TextFormatting
import kotlinx.android.synthetic.main.user_statement_layout.view.*

class StatementViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val textFormatting = TextFormatting()

    fun bind(model: Statements) {
        itemView.transactionTypeHeaderTextView.text = model.title
        itemView.paymentTypeTextView.text = model.description
        itemView.transactionDateTextView.text = textFormatting.formatDate(model.date)

        when {

            model.value < 0 -> {
                setTextColor(itemView.ownerAccountBalanceTextView, R.color.red)
            }

            else -> {
                setTextColor(itemView.ownerAccountBalanceTextView, R.color.green)
            }
        }

        itemView.ownerAccountBalanceTextView.text = textFormatting.formatCurrency(model.value)
    }

    private fun setTextColor(textView: TextView, color: Int) {
        textView.setTextColor(
            ContextCompat.getColor(
                itemView.context,
                color
            )
        )
    }
}