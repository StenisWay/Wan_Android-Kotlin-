package com.stenisway.wan_android.ui.categories.repo

import com.stenisway.wan_android.ui.newitem.newsbean.NewItemBean
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CategoriesDetailRepository {

    private val _categoriesItemBeanFlow = MutableSharedFlow<NewItemBean>(replay = 1)
    val categoriesItemBeanFlow = _categoriesItemBeanFlow.asSharedFlow()

    suspend fun submitCategoriesItemBean(newItemBean: NewItemBean){
        _categoriesItemBeanFlow.emit(newItemBean)
    }

}