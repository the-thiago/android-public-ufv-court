package com.ufv.court.core.core_common.util

import java.net.URLEncoder
import java.util.*

fun String.toEncodedString(): String {
    return URLEncoder.encode(this, "UTF-8")
}

fun String.toMaskedPhone(): String {
    return if (this.length == 11) {
        var formattedPhone = ""
        this.forEachIndexed { index, c ->
            when (index) {
                0 -> formattedPhone += "("
                2 -> formattedPhone += ") "
                7 -> formattedPhone += "-"
            }
            formattedPhone += c
        }
        formattedPhone
    } else {
        this
    }
}

object ScheduleUtils {
    fun getTimeInMillisFromDate(day: Int, month: Int, year: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.HOUR_OF_DAY, 1)
        calendar.set(Calendar.MINUTE, 1)
        calendar.set(Calendar.SECOND, 1)
        calendar.set(Calendar.MILLISECOND, 1)
        calendar.set(Calendar.HOUR, 1)
        calendar.timeZone = TimeZone.getTimeZone("UTC")
        calendar.set(Calendar.ZONE_OFFSET, 0)
        calendar.set(Calendar.DST_OFFSET, 0)
        calendar.set(Calendar.AM_PM, 1)
        return calendar.timeInMillis
    }
}
