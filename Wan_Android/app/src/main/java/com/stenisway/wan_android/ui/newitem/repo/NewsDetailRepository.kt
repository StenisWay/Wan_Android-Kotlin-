package com.stenisway.wan_android.ui.newitem.repo

import com.stenisway.wan_android.ui.newitem.newsbean.NewItem
import com.stenisway.wan_android.ui.newitem.newsbean.NewItems
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NewsDetailRepository {

    private val _localItem = MutableSharedFlow<NewItem>(replay = 0)
    val localItem = _localItem.asSharedFlow()

    suspend fun submitLocalItem(localItem : NewItem){
        _localItem.emit(localItem)
    }

}