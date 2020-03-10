package br.com.caiodev.walletapp.sections.userStatements.model.viewTypes

import br.com.caiodev.walletapp.utils.viewType.RecyclerViewViewTypes
import br.com.caiodev.walletapp.utils.viewType.ViewType

data class Header(val headerName: String) : ViewType {

    override fun getViewType(): Int {
        return RecyclerViewViewTypes.header
    }
}