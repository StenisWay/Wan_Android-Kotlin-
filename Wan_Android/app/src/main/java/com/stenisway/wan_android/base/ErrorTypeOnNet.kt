package com.stenisway.wan_android.base

sealed class ErrorTypeOnNet  {

    data class NewItemErrorOnNet(val page : Int, val err : Throwable) : ErrorTypeOnNet()
    data class CategoriesTitleAndItemErrorOnNet(val err : Throwable) : ErrorTypeOnNet()
    data class HotKeyErrorOnNet(val err : Throwable) : ErrorTypeOnNet()
    data class BannerOnNetError(val err : Throwable) : ErrorTypeOnNet()
    data class CategoriesDataErrorOnNet (val page : Int, val id : Int, val err : Throwable) : ErrorTypeOnNet()


}