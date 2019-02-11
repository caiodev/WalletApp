package br.com.caiodev.walletapp.sections.statement.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.caiodev.walletapp.sections.statement.model.Header
import br.com.caiodev.walletapp.sections.statement.model.Statement
import br.com.caiodev.walletapp.sections.statement.model.UserStatement
import br.com.caiodev.walletapp.sections.statement.model.repository.StatementRepository
import br.com.caiodev.walletapp.utils.ViewType
import br.com.caiodev.walletapp.utils.service.APICallResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StatementViewModel : ViewModel() {

    val state = MutableLiveData<Int>()
    private var statementListValues = mutableListOf<ViewType>()
    private val scope = CoroutineScope(Job() + Dispatchers.Default)
    private val repository = StatementRepository()

    fun getStatement() {

        scope.launch {

            val value = repository.getUserStatement()

            when (value) {

                is APICallResult.Success<*> -> with(value.data as UserStatement) {

                    statementListValues.clear()
                    statementListValues.add(Header("Recent"))

                    //Sort list by Date


                    statementList.forEach { statementList ->
                        statementListValues.add(
                            Statement(
                                statementList.date,
                                statementList.description,
                                statementList.title,
                                statementList.value
                            )
                        )
                    }

                    state.postValue(onStatementRetrievalSuccess)
                }

                else -> {
                    state.postValue(onStatementRetrievalError)
                }
            }
        }
    }

    fun getStatementList() = statementListValues

    companion object {
        const val onStatementRetrievalSuccess = 0
        const val onStatementRetrievalError = 1
    }
}