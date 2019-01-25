package br.com.caiodev.walletapp.statements.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.caiodev.walletapp.repository.StatementRepository
import br.com.caiodev.walletapp.statements.model.Statement
import br.com.caiodev.walletapp.statements.model.UserStatement
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StatementViewModel : ViewModel() {

    val state = MutableLiveData<Int>()
    private var statementList = mutableListOf<Statement>()
    private val coroutineContext = Job() + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)
    private val repository = StatementRepository()

    fun getStatement() {

        scope.launch {

            val value = repository.getUserStatement()

            when (value) {

                is Boolean -> {
                    state.postValue(onStatementRetrievalError)
                }

                else -> {
                    if (value is UserStatement) {
                        statementList = value.statementList
                        state.postValue(onStatementRetrievalSuccess)
                    }
                }
            }
        }
    }

    fun getStatementList() = statementList

    fun <T> getHawkValue(key: String) = Hawk.get(key) as T

    fun deleteHawkValue(key: String) {
        Hawk.delete(key)
    }

    companion object {
        const val onStatementRetrievalSuccess = 0
        const val onStatementRetrievalError = 1
    }
}