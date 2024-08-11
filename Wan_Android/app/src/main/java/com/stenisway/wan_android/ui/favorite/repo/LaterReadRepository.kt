package com.stenisway.wan_android.ui.favorite.repo

import com.stenisway.wan_android.ui.newitem.newsbean.NewItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class LaterReadRepository {

    private val _laterReadItems = MutableSharedFlow<List<NewItem>>()
    val laterReadItems = _laterReadItems.asSharedFlow()

    suspend fun submitLaterReadItems( items : List<NewItem>){
        _laterReadItems.emit(items)
    }



}