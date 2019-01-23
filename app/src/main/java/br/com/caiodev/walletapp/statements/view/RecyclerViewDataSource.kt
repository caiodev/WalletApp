package br.com.caiodev.walletapp.statements.view

interface RecyclerViewDataSource<out T> {
    fun getTotalCount(): Int
    fun getItem(position: Int): T
}