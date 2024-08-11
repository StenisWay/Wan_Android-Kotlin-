package com.stenisway.wan_android.ui.categories.repo

import android.util.Log
import com.stenisway.wan_android.ui.categories.categoriesbean.CgItem
import com.stenisway.wan_android.ui.categories.categoriesbean.CgTitle
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CategoriesRepository {


    private val _cgTilesFlow = MutableSharedFlow<List<CgTitle>>(replay = 1)
    val cgTitlesFlow = _cgTilesFlow.asSharedFlow()

    suspend fun submitCgTitles(cgTitles: List<CgTitle>){
        Log.d("submitCgTitle", "$cgTitles")
        _cgTilesFlow.emit(cgTitles)
    }

    private val _cgItemsFlow = MutableSharedFlow<List<CgItem>>(replay = 1)
    val cgItemsFlow = _cgItemsFlow.asSharedFlow()

    suspend fun submitCgItems(cgItems: List<CgItem>){
        _cgItemsFlow.emit(cgItems)
    }

}