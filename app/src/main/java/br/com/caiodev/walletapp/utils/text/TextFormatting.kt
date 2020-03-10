package br.com.caiodev.walletapp.utils.text

import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TextFormatting {

    private val datePattern = "yyyy-MM-dd"
    private val numberFormatInstance =
        NumberFormat.getCurrencyInstance(Locale.getDefault()) as DecimalFormat
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

    fun concatenateStrings(template: String, vararg text: String) = String.format(template, *text)
}