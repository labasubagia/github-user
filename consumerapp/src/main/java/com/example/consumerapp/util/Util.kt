package com.example.consumerapp.util

import java.text.NumberFormat
import java.util.*

object Util {
    // function to format thousand number string
    fun numberFormat(number: Int): String {
        // use comma separator
        val formatter = NumberFormat.getInstance(Locale.US)
        return formatter.format(number)
    }
}
