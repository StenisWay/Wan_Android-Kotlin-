package com.stenisway.wan_android.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stenisway.wan_android.ui.newitem.newsbean.NewItem
import com.stenisway.wan_android.ui.newitem.newsbean.NewItemBean
import com.stenisway.wan_android.util.retrofitutil.MyAPIService
import com.stenisway.wan_android.util.retrofitutil.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    var needToScrollToTop = true
    private val _search: MutableLiveData<List<NewItem>> = MutableLiveData<List<NewItem>>()
    val search: LiveData<List<NewItem>>
        get() = _search
    private var CURRENT_PAGE = -1
    private val TAG = this.javaClass.name
    var SEARCH_ISLOADING = false
    var TOTAL_PAGE = 1
    fun getSearchData(search: String?) {
        if (SEARCH_ISLOADING) {
            return
        }
        SEARCH_ISLOADING = true
        CURRENT_PAGE += 1
        if (CURRENT_PAGE > TOTAL_PAGE) {
            return
        }
        Log.d(TAG + "process_here_search)", "有執行到search")
//        val myAPIService: MyAPIService = RetrofitUtil.getInstance().getAPI()
//        val call = myAPIService.getSearchUrl(CURRENT_PAGE, search)
//        call!!.enqueue(object : Callback<NewItemBean?> {
//            override fun onResponse(call: Call<NewItemBean?>, response: Response<NewItemBean?>) {
//                Log.d(TAG + "search_connectSuccess", "123")
//                if (response.isSuccessful) {
//                    assert(response.body() != null)
//                    Log.d(TAG + "search_connectSuccess", response.body()!!.data!!.datas.toString())
//                    assert(response.body() != null)
//                    if (TOTAL_PAGE != 1) {
//                        TOTAL_PAGE = response.body()!!.data!!.pageCount
//                    }
//                    val searchData: List<New_Item> = response.body()!!.data!!.datas
//                    val item = response.body()
//                    TOTAL_PAGE = response.body()!!.data!!.pageCount
//                    //                    如果取出的值為0或是null，很有可能是json格式解析錯誤
//                    Log.d(
//                        TAG + "search_connectSuccess total",
//                        "onResponse: " + item!!.data!!.pageCount
//                    )
//                    val newItemList: List<New_Item>? = _search.value
//                    if (newItemList != null && !searchData.isEmpty()) {
//                        newItemList.addAll(searchData)
//                        _search.setValue(newItemList)
//                    } else {
//                        _search.setValue(searchData)
//                    }
//                } else {
//                    Log.d(TAG + "search_connectFail", "responseFail")
//                }
//            }
//
//            override fun onFailure(call: Call<NewItemBean?>, t: Throwable) {
//                Log.d("search_connectFail", t.toString())
//            }
//        })
        SEARCH_ISLOADING = false
    }
}