package br.com.caiodev.walletapp.utils

import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar

abstract class MasterActivity : AppCompatActivity() {

    protected abstract fun setupView()
    protected abstract fun setupViewModel()
    protected abstract fun handleViewModel()

    protected fun setViewVisibility(view: View, visibility: Int? = null) {

        when (view) {

            is ProgressBar -> {
                visibility?.let {
                    view.visibility = it
                }
            }

            is SwipeRefreshLayout -> {
                view.isRefreshing = false
            }
        }
    }

    protected fun showSnackBar(
        message: String
    ) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }
}