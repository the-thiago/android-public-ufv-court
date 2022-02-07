package com.ufv.court.core.core_common.util

import java.net.URLEncoder

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
