package com.stenisway.wan_android.util

import android.content.Context
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment

fun Fragment.getScreenWidthAndHeight(context: Context): Pair<Int, Int> {

    val outMetrics = DisplayMetrics()

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        val display = activity?.display
        display?.getRealMetrics(outMetrics)
    } else {
        @Suppress("DEPRECATION")
        val display = activity?.windowManager?.defaultDisplay
        @Suppress("DEPRECATION")
        display?.getMetrics(outMetrics)
    }

    val width = outMetrics.widthPixels
    val height = outMetrics.heightPixels

    return Pair(width, height)
}