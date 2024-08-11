package com.stenisway.wan_android.ui.newitem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.stenisway.wan_android.ui.newitem.newsbean.NewItem
import com.stenisway.wan_android.ui.newitem.repo.NewsDetailRepository
import com.stenisway.wan_android.util.roomutil.CRUDRepository
import com.stenisway.wan_android.util.roomutil.withIO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(application: Application, val repository: NewsDetailRepository, val crudRepository: CRUDRepository) :
    AndroidViewModel(application) {
    private val _netItem: MutableLiveData<NewItem> = MutableLiveData<NewItem>()
    val newItem : LiveData<NewItem>
        get() = _netItem

    fun setNetItem(item: NewItem) {
        _netItem.value = item
    }

    fun getLocalItem(item: NewItem){
        withIO {
            crudRepository.getLocalItem(item)
        }
    }

    fun updateNewItem(newItem : NewItem) {
        withIO {
           crudRepository.updateNewItem(newItem)
        }
    }

}
