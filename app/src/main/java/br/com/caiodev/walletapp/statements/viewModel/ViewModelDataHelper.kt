package br.com.caiodev.walletapp.statements.viewModel

import br.com.caiodev.walletapp.statements.model.Statement
import br.com.caiodev.walletapp.statements.view.RecyclerViewDataSource

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