package br.com.caiodev.walletapp.sections.statement.model

import br.com.caiodev.walletapp.utils.RecyclerViewViewTypes
import br.com.caiodev.walletapp.utils.ViewType
import com.squareup.moshi.Json

data class Statement(
    @field:Json(name = "date") val date: String = "",
    @field:Json(name = "desc") val description: String = "",
    @field:Json(name = "title") val title: String = "",
    @field:Json(name = "value") val value: Double = 0.0
) : ViewType {

    override fun getViewType(): Int {
        return RecyclerViewViewTypes.userStatements
    }
}