package com.stenisway.wan_android.util

class PageUtil {
    var needToScrollToTop = true
    var CURRENT_PAGE = -1
    var ISLOADING = false
    var TOTAL_PAGE = 1


    fun isOverPage() : Boolean{
        return CURRENT_PAGE > TOTAL_PAGE
    }
}