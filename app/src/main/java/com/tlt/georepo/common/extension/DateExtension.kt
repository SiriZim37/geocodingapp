package com.tlt.georepo.common.extension

import java.text.SimpleDateFormat
import java.util.*

fun Date.toDatetimeByPattern(pattern: String = "dd MMM yyyy - HH:mm"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.US)
    return dateFormat.format(this)
}

fun Date.getYears(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.YEAR).toString()
}