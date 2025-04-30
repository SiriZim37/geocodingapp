package com.tlt.georepo.common.extension

import com.tlt.georepo.manager.LocalizeManager
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun String.toDateByPattern(pattern: String = "dd/MM/yyyy HH:mm:ss"): Date {
    val date = if (this.isEmpty()) {
        "01/01/2118 01:01:01"
    } else if (this.length == 10) {
        "$this 23:59:00"
    } else {
        this
    }

    val dateFormat = SimpleDateFormat(pattern, Locale.US)

    return dateFormat.parse(date)
}

/**
 * Change Date Format
 * Example Eng : 09/05/2018 > 9 May 2018
 * Example Th : 09/05/2018 > 9 ม.ย. 2561
 */

fun String.toDatetime(): String {
    if (this.trim().isEmpty()) {
        return "-"
    }

    val outputLocale = if (LocalizeManager.isThai()) {
        Locale("th", "TH")
    } else {
        Locale.ENGLISH
    }

    val inputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    val inputDate = inputDateFormat.parse(this)

    return if (LocalizeManager.isThai()) {
        Calendar.getInstance().let {
            it.time = inputDate
            it.add(Calendar.YEAR, 543)

            SimpleDateFormat("dd MMM yyyy", outputLocale).format(it.time)
        }
    } else {
        SimpleDateFormat("dd MMM yyyy", outputLocale).format(inputDate)
    }
}

fun String.toDatetimeReceipt(): String {
    if (this.trim().isEmpty()) {
        return "-"
    }

    val outputLocale = if (LocalizeManager.isThai()) {
        Locale("th", "TH")
    } else {
        Locale.ENGLISH
    }

    val inputDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH)
    val inputDate = inputDateFormat.parse(this)

    return if (LocalizeManager.isThai()) {
        Calendar.getInstance().let {
            it.time = inputDate
            it.add(Calendar.YEAR, 543)

            SimpleDateFormat("dd MMM yyyy - HH:mm น.", outputLocale).format(it.time)
        }
    } else {
        SimpleDateFormat("dd MMM yyyy - HH:mm", outputLocale).format(inputDate)
    }
}

fun String.toTwitterDatetime(): String {
    if (this.trim().isEmpty()) {
        return "-"
    }

    val outputLocale = if (LocalizeManager.isThai()) {
        Locale("th", "TH")
    } else {
        Locale.ENGLISH
    }

    val inputDateFormat = SimpleDateFormat("E MMM d HH:mm:ss Z yyyy", Locale.ENGLISH)
    val inputDate = inputDateFormat.parse(this)

    /*return if (LocalizeManager.isThai()) {
        Calendar.getInstance().let {
            it.time = inputDate
            it.add(Calendar.YEAR, 543)

            SimpleDateFormat("d MMM yyyy - HH.mm น.", outputLocale).format(it.time)
        }
    } else {
        SimpleDateFormat("d MMM yyyy - HH.mm น.", outputLocale).format(inputDate)
    }*/

    return if (LocalizeManager.isThai()) {
        Calendar.getInstance().let {
            it.time = inputDate
            it.add(Calendar.YEAR, 543)

            SimpleDateFormat("d MMM yyyy - HH.mm น.", outputLocale).format(it.time)
        }
    } else {
        SimpleDateFormat("d MMM yyyy - HH.mm", outputLocale).format(inputDate)
    }
}

fun Long.toMinsAndSecs(): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))

    val numberFormat = DecimalFormat("00")

    return "${numberFormat.format(minutes)}:${numberFormat.format(seconds)}"
}

fun Date.toCurrentByUTC(): Date {
    val utcTimezone = TimeZone.getTimeZone("UTC")
    return Calendar.getInstance(utcTimezone).time
}
