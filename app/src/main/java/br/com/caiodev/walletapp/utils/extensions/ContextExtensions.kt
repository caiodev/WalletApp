package br.com.caiodev.walletapp.utils.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar

@Suppress("UNUSED")
fun Context.setViewVisibility(view: View, visibility: Int? = null) {

    when (view) {

        is SwipeRefreshLayout -> {
            if (view.isRefreshing) view.isRefreshing = false
        }

        else -> {
            visibility?.let {
                view.visibility = it
            }
        }
    }
}

@Suppress("UNUSED")
fun FragmentActivity.showSnackBar(
    fragmentActivity: FragmentActivity, message: String
) {
    Snackbar.make(
        fragmentActivity.findViewById(android.R.id.content),
        message,
        Snackbar.LENGTH_LONG
    ).show()
}

@Suppress("UNUSED")
fun FragmentActivity.hideKeyboard(context: Context, view: View) {
    with(context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager) {
        hideSoftInputFromWindow(view.windowToken, 0)
    }
}

@Suppress("UNUSED")
fun FragmentActivity.setViewXYScales(view: View, xAxis: Float, yAxis: Float) {
    view.scaleX = xAxis
    view.scaleY = yAxis
}

@Suppress("UNUSED")
fun FragmentActivity.setEditTextError(editText: EditText, errorMessage: String) {
    editText.error = errorMessage
}