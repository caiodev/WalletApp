package br.com.caiodev.walletapp.sections.statement.view

interface RecyclerViewDataSource<out T> {
    fun getTotalCount(): Int
    fun getItem(position: Int): T
}