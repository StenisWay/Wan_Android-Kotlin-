package com.stenisway.wan_android.util

import android.text.TextUtils

class StringUtil {
    fun isInvalid(text: String): Boolean {
        return TextUtils.isEmpty(text) || text.contains(" ")
    }

    fun replaceInvalidChar(text: String): String {
        return text.replace("<em class='highlight'>", "")
            .replace("</em>", "")
            .replace("&mdash;", "-")
            .replace("&ndash;", "-")
            .replace("&ldquo;", "'")
            .replace("&rdquo;", "'")
            .replace("&amp;", "&")
    }
}
