package br.com.caiodev.walletapp.sections.userStatements.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.caiodev.walletapp.R
import br.com.caiodev.walletapp.sections.userStatements.model.viewTypes.Header
import br.com.caiodev.walletapp.sections.userStatements.model.viewTypes.Statements
import br.com.caiodev.walletapp.sections.userStatements.view.view_holder.HeaderViewHolder
import br.com.caiodev.walletapp.sections.userStatements.view.view_holder.StatementViewHolder
import br.com.caiodev.walletapp.utils.viewType.RecyclerViewViewTypes
import br.com.caiodev.walletapp.utils.viewType.ViewType

class StatementAdapter(data: MutableList<ViewType>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataSource = data

    override fun getItemCount() = itemCount()

    override fun getItemViewType(position: Int) = itemViewType(position).getViewType()

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

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is HeaderViewHolder -> viewHolder.bind(itemViewType(position) as Header)
            is StatementViewHolder -> viewHolder.bind(itemViewType(position) as Statements)
        }
    }

    internal fun updateDataSource(newDataSource: MutableList<ViewType>) {
        dataSource = newDataSource
    }

    private fun itemCount() = dataSource.size
    private fun itemViewType(position: Int) = dataSource[position]

    //TODO: Implement a checking to see if the received value is different than the current one using DiffUtil
}