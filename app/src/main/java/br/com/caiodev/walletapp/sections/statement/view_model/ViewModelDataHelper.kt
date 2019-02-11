package br.com.caiodev.walletapp.sections.statement.view_model

import br.com.caiodev.walletapp.sections.statement.view.RecyclerViewDataSource
import br.com.caiodev.walletapp.utils.ViewType

class ViewModelDataHelper : RecyclerViewDataSource<ViewType> {

    private var statementList = mutableListOf<ViewType>()

    fun getList(list: MutableList<ViewType>) {
        statementList = list
    }

    override fun getTotalCount(): Int {
        return statementList.size
    }

    override fun getItem(position: Int): ViewType {
        return statementList[position]
    }
}