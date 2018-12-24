package br.com.caiodev.walletapp.utils

import androidx.appcompat.app.AppCompatActivity

abstract class MasterActivity : AppCompatActivity() {

    protected abstract fun setupView()
    protected abstract fun setupViewModel()
    protected abstract fun bindViewToViewModel()
}