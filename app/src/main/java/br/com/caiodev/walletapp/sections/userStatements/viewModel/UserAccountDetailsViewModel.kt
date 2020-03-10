package br.com.caiodev.walletapp.sections.userStatements.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.caiodev.walletapp.sections.login.model.UserAccount
import br.com.caiodev.walletapp.sections.userStatements.model.repository.StatementRepository
import br.com.caiodev.walletapp.sections.userStatements.model.viewTypes.Header
import br.com.caiodev.walletapp.sections.userStatements.model.viewTypes.Statements
import br.com.caiodev.walletapp.utils.base.apiResponse.BaseAPIResponse
import br.com.caiodev.walletapp.utils.dataRecoveryIds.HawkIds
import br.com.caiodev.walletapp.utils.extensions.getHawkValue
import br.com.caiodev.walletapp.utils.service.APICallResult
import br.com.caiodev.walletapp.utils.viewType.ViewType
import kotlinx.coroutines.launch
import timber.log.Timber

class UserAccountDetailsViewModel : ViewModel() {

    private val state = MutableLiveData<Any>()
    private var statementListValues = mutableListOf<ViewType>()
    private val repository = StatementRepository()

    init {
        getStatement()
    }

    fun getStatement() {

        viewModelScope.launch {

            getHawkValue<UserAccount>(HawkIds.userAccountData).userId.let { userId ->

                Timber.i("USERID: %s", userId)
                val value = repository.getUserStatement(userId)

                @Suppress("UNCHECKED_CAST")
                when (value) {

                    is APICallResult.Success<*> -> with(value.data as BaseAPIResponse) {

                        statementListValues.clear()

                        statementListValues.add(
                            Header(
                                "Recent"
                            )
                        )

                        this.statementList.sortByDescending { it.date }

                        this.statementList.forEach { statement ->
                            populateList(statement)
                        }

                        Timber.i("CASTEXCEPTION: %s", "APICallResult.Success")
                        state.postValue(statementListValues)
                    }

                    is APICallResult.ProcessingError<*> -> {
                        Timber.i("CASTEXCEPTION: %s", "APICallResult.ProcessingError")
                        with(value.error as BaseAPIResponse) {
                            Timber.i("CASTEXCEPTION: %s", this.error.message)
                            state.postValue(this.error.message)
                        }
                    }

                    is APICallResult.ConnectionError -> state.postValue(onInternetConnectionError)

                    else -> state.postValue(onAPIConnectionError)
                }
            }
        }
    }

    private fun populateList(statements: Statements) {
        statementListValues.add(
            Statements(
                statements.date,
                statements.description,
                statements.title,
                statements.value
            )
        )
    }

    fun getDataTypeState() = state

    companion object {
        const val onInternetConnectionError = 1
        const val onAPIConnectionError = 2
    }
}