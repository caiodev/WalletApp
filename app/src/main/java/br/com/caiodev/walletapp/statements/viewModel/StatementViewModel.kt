package br.com.caiodev.walletapp.statements.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.caiodev.walletapp.factory.RetrofitService
import br.com.caiodev.walletapp.service.BankService
import br.com.caiodev.walletapp.statements.model.Statement
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class StatementViewModel : ViewModel() {

    private var statementList = mutableListOf<Statement>()
    private val service: BankService = RetrofitService().createService()
    val state = MutableLiveData<Int>()

    fun getStatement() {

        GlobalScope.launch(Dispatchers.Default) {

            val request = service.getUserDetails()
            val response = request.await()

            when {

                request.isCompleted -> {
                    statementList = response.statementList
                    state.postValue(onStatementRetrievalSuccess)
                }

                request.isCompletedExceptionally -> {

                    state.postValue(onStatementRetrievalError)

                    Timber.i(
                        "APIError: %s",
                        request.getCompletionExceptionOrNull().toString()
                    )
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