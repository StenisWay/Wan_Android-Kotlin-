package com.stenisway.wan_android.util.retrofitutil

object Api {
    const val baseUrl = "https://www.wanandroid.com/"

    //  基本的Url
    const val NewsUrl = "article/list"

    //  最新文章
    const val BannerUrl = "banner/json"

    //  廣告板
    const val Categories = "tree/json"

    //    分類項的標題
    const val CategoriesDetail = "article/list"

    //    分類項的細項(id要從上面的標題api取得)
    const val HotKeys = "hotkey/json"

    //    搜尋熱鍵
    const val Search = "article/query"
}