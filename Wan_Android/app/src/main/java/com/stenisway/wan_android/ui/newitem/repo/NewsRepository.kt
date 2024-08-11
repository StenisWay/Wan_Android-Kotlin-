package com.stenisway.wan_android.ui.newitem.repo

import android.util.Log
import com.stenisway.wan_android.component.banner.bannerbean.BannerItem
import com.stenisway.wan_android.ui.newitem.newsbean.NewItems
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NewsRepository {

    private val _newsData = MutableSharedFlow<NewItems>(  replay = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1)
    val newData
        get() = _newsData.asSharedFlow()

    suspend fun submitNewData(newItems: NewItems){
        Log.d("newDataEmit", "submitNewData")
        _newsData.emit(newItems)
    }

    private val _bannerData = MutableSharedFlow<List<BannerItem>>(replay = 1)
    val bannerData
        get() = _bannerData.asSharedFlow()

    suspend fun submitBannerData(bannerItems: List<BannerItem>){
        _bannerData.emit(bannerItems)
    }

}