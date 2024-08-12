package com.stenisway.wan_android.ui.categories.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stenisway.wan_android.ui.categories.repo.CategoriesDetailRepository
import com.stenisway.wan_android.ui.newitem.newsbean.NewItem
import com.stenisway.wan_android.ui.newitem.newsbean.NewItemBean
import com.stenisway.wan_android.util.PageUtil
import com.stenisway.wan_android.util.retrofitutil.MyAPIService
import com.stenisway.wan_android.util.retrofitutil.RetrofitRequest
import com.stenisway.wan_android.util.retrofitutil.RetrofitUtil
import com.stenisway.wan_android.util.roomutil.CRUDRepository
import com.stenisway.wan_android.util.roomutil.withIO
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class CategoriesDetailViewModel @Inject constructor(val categoriesDetailRepository: CategoriesDetailRepository, private val request: RetrofitRequest, private val crudRepository: CRUDRepository): ViewModel() {

    val page = PageUtil()
    private val TAG = this.javaClass.name
    var id = -1

    private var _cgData = mutableListOf<NewItem>()

    fun getCategoriesData(_id: Int = id) {
        if (page.ISLOADING) {
            return
        }
        page.ISLOADING = true
        page.CURRENT_PAGE += 1
        if (page.isOverPage()) {
            return
        }
        withIO {
            request.getCategoriesData(_id, page.CURRENT_PAGE)
        }
    }

    fun clearData(){
        _cgData = mutableListOf()
    }


    fun getAllData( datas : List<NewItem> = emptyList() ) : List<NewItem>{
        _cgData.addAll(datas)
        val newsData = mutableListOf<NewItem>()
        newsData.addAll(_cgData)
        return newsData
    }
}