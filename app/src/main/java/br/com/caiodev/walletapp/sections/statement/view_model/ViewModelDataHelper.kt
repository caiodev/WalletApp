package br.com.caiodev.walletapp.sections.statement.view_model

import br.com.caiodev.walletapp.sections.statement.model.Statement
import br.com.caiodev.walletapp.sections.statement.view.RecyclerViewDataSource

class ViewModelDataHelper : RecyclerViewDataSource<Statement> {

    private var statementList = mutableListOf<Statement>()

    fun listReceiver(list: MutableList<Statement>) {
        statementList.clear()
        statementList = list
    }

    override fun getTotalCount(): Int {
        return statementList.size
    }

    override fun getItem(position: Int): Statement {
        return statementList[position]
    }
}