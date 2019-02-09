package br.com.caiodev.walletapp.sections.statement.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.caiodev.walletapp.R
import br.com.caiodev.walletapp.sections.statement.model.Header
import br.com.caiodev.walletapp.sections.statement.model.Statement
import br.com.caiodev.walletapp.sections.statement.view.view_holder.HeaderViewHolder
import br.com.caiodev.walletapp.sections.statement.view.view_holder.StatementViewHolder
import br.com.caiodev.walletapp.utils.RecyclerViewViewTypes
import br.com.caiodev.walletapp.utils.ViewType

class StatementAdapter(private val data: RecyclerViewDataSource<ViewType>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return data.getItem(position).getViewType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val itemView = LayoutInflater.from(parent.context)

        return when (viewType) {

            RecyclerViewViewTypes.header -> {
                HeaderViewHolder(
                    itemView.inflate(
                        R.layout.header_layout,
                        parent,
                        false
                    )
                )
            }

            else -> {
                StatementViewHolder(
                    itemView.inflate(
                        R.layout.user_statement_layout,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount() = data.getTotalCount()

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is HeaderViewHolder -> viewHolder.bind(data.getItem(position) as Header)
            is StatementViewHolder -> viewHolder.bind(data.getItem(position) as Statement)
        }
    }
}