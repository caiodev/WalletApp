package br.com.caiodev.walletapp.sections.statement.view.view_holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.caiodev.walletapp.sections.statement.model.Header
import kotlinx.android.synthetic.main.header_layout.view.*

class HeaderViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(model: Header) {
        itemView.headerType.text = model.headerName
    }
}