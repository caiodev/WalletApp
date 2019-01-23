package br.com.caiodev.walletapp.statements.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.caiodev.walletapp.R
import br.com.caiodev.walletapp.statements.model.Statement

class StatementAdapter(private val data: RecyclerViewDataSource<Statement>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StatementViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_item_layout,
            parent,
            false
        )
    )

    override fun getItemCount() = data.getTotalCount()

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is StatementViewHolder -> viewHolder.bind(data.getItem(position))
        }
    }
}