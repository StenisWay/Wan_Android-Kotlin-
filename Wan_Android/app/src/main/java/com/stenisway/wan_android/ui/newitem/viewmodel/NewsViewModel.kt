package com.stenisway.wan_android.ui.newitem.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stenisway.wan_android.ui.newitem.newsbean.NewItem
import com.stenisway.wan_android.ui.newitem.repo.NewsRepository
import com.stenisway.wan_android.util.PageUtil
import com.stenisway.wan_android.util.retrofitutil.RetrofitRequest
import com.stenisway.wan_android.util.roomutil.CRUDRepository
import com.stenisway.wan_android.util.roomutil.withIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(application: Application, val repository : NewsRepository, private val retrofitRequest : RetrofitRequest, private val crudRepository: CRUDRepository) : AndroidViewModel(application) {
    private val TAG = this.javaClass.name
    val page = PageUtil()

    private val _newsData = mutableListOf<NewItem>()

    fun getBannerData(){
        withIO {
            crudRepository.getBannerItemFromLocalToNewsFragment()
        }
    }

    fun getNewsData(){
        withIO {

            if (page.ISLOADING) {
                return@withIO
            }
            page.ISLOADING = true
            page.CURRENT_PAGE += 1
            Log.d(TAG, "getNewsData: ${page.isOverPage()}")
            if (page.isOverPage()) {
                return@withIO
            }
            retrofitRequest.getNewsData(page.CURRENT_PAGE)
            Log.d(TAG, "getNewsData: 有執行")



        }
    }

    fun getAllData( datas : List<NewItem> ) : List<NewItem>{
        _newsData.addAll(datas)
        val newsData = mutableListOf<NewItem>()
        newsData.addAll(_newsData)
        return newsData
    }

}