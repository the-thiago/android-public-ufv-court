package com.ufv.court.core.core_common.util

import java.net.URLEncoder

fun String.toEncodedString(): String {
    return URLEncoder.encode(this, "UTF-8")
}
