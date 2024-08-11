package com.stenisway.wan_android.activity

import com.stenisway.wan_android.component.banner.bannerbean.BannerItems
import com.stenisway.wan_android.ui.categories.categoriesbean.CgBean
import com.stenisway.wan_android.ui.categories.categoriesbean.CgItem
import com.stenisway.wan_android.ui.categories.categoriesbean.CgTitle
import com.stenisway.wan_android.ui.search.hkbean.HokeyItems
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class MainActivityRepository {

    private val _bannerFlow = MutableSharedFlow<BannerItems>(replay = 1)
    val bannerFlow = _bannerFlow.asSharedFlow()

    private val _hokeyItems = MutableSharedFlow<HokeyItems>(replay = 1)
    val hokeyItems = _hokeyItems.asSharedFlow()

    private val _cgFlow = MutableSharedFlow<CgBean>(replay = 1)
    val cgFlow = _cgFlow.asSharedFlow()

    suspend fun submitCgBean(cgBeans: CgBean){
        _cgFlow.emit(cgBeans)
    }


    suspend fun submitBannerItems(bannerItems: BannerItems){
        _bannerFlow.emit(bannerItems)
    }

    suspend fun submitHokeyItems(hokeyItems: HokeyItems){
        _hokeyItems.emit(hokeyItems)
    }

}