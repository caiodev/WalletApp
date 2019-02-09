package br.com.caiodev.walletapp.utils

import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TextFormatting {

    private val numberFormatInstance =
        NumberFormat.getCurrencyInstance(Locale.getDefault()) as DecimalFormat
    private val datePattern = "yyyy-MM-dd"
    private val simpleDateFormat = SimpleDateFormat(datePattern, Locale.getDefault())
    private val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)

    fun formatCurrency(value: Double): String {
        numberFormatInstance.apply {
            negativePrefix = "-${numberFormatInstance.currency.symbol}"
            negativeSuffix = ""
            return format(value)
        }
    }

    fun formatDate(date: String): String = dateFormat.format(simpleDateFormat.parse(date))
}