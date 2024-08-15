package com.stenisway.wan_android.ui.favorite.repo

import android.util.Log
import com.stenisway.wan_android.ui.newitem.newsbean.NewItem
import com.stenisway.wan_android.ui.newitem.newsbean.NewItems
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.math.log

class FavoriteRepository {

    private val _favoriteItems = MutableSharedFlow<List<NewItem>>()
    val favoriteItems = _favoriteItems.asSharedFlow()

    suspend fun submitFavoriteItems( items : List<NewItem>){
        _favoriteItems.emit(items)
    }



}