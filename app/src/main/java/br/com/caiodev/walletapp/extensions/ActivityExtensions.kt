package br.com.caiodev.walletapp.extensions

import android.content.Context
import android.view.View

fun Context.setViewVisibility(view: View, visibility: Int) {
    view.visibility = visibility
}