package com.stenisway.wan_android.util.retrofitutil

import android.content.Context
import android.util.Log
import com.stenisway.wan_android.activity.MainActivityRepository
import com.stenisway.wan_android.base.ErrorTypeOnNet
import com.stenisway.wan_android.component.banner.bannerbean.BannerItems
import com.stenisway.wan_android.ui.categories.categoriesbean.CgBean
import com.stenisway.wan_android.ui.categories.repo.CategoriesDetailRepository
import com.stenisway.wan_android.ui.newitem.newsbean.NewItemBean
import com.stenisway.wan_android.ui.newitem.newsbean.NewItems
import com.stenisway.wan_android.ui.newitem.repo.NewsRepository
import com.stenisway.wan_android.ui.search.hkbean.HokeyItems
import com.stenisway.wan_android.util.roomutil.withIO
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RetrofitRequest (val context: Context) {

    val TAG = "RetrofitRequest"

    val myAPIService: MyAPIService = RetrofitUtil.instance.api

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface RepositoryEntryPoint {
        fun productNewsRepository(): NewsRepository
        fun productMainRepository(): MainActivityRepository
        fun productCategoriesDetailRepository() : CategoriesDetailRepository
    }

    private val appContext = context.applicationContext ?: throw IllegalStateException()
    private val hiltEntryPoint =
        EntryPointAccessors.fromApplication(appContext, RepositoryEntryPoint::class.java)
    val newsRepository = hiltEntryPoint.productNewsRepository()
    val mainRepository = hiltEntryPoint.productMainRepository()
    val categoriesDetailRepository = hiltEntryPoint.productCategoriesDetailRepository()

    fun getNewsData(currentPage: Int){
        val call = myAPIService.getNewUrl(currentPage)
        call!!.enqueue(object : Callback<NewItemBean?> {
            override fun onResponse(
                call: Call<NewItemBean?>,
                response: Response<NewItemBean?>
            ) {
                if (response.isSuccessful) {
                    assert(response.body() != null)
                    Log.d(TAG, "onResponse: getNewItems Successful")
                    val newsData: NewItems = response.body()!!.data!!
                    withIO {
                        newsRepository.submitNewData(newsData)
                    }

                }
            }

            override fun onFailure(call: Call<NewItemBean?>, t: Throwable) {
                Log.e(TAG + "connectFail", "connectFail  $t")

                withIO {
                    mainRepository.submitErrorEvent(ErrorTypeOnNet.NewItemErrorOnNet(currentPage, t))
                }
            }
        })
    }

    fun getHkOnNet(): Boolean {
        val successful = false
        val call = myAPIService.hotKeys
        call!!.enqueue(object : Callback<HokeyItems?> {
            override fun onResponse(call: Call<HokeyItems?>, response: Response<HokeyItems?>) {
                assert(response.body() != null)
                val items = response.body()
                withIO {
                    items?.let { mainRepository.submitHokeyItems(it) }
                }
            }

            override fun onFailure(call: Call<HokeyItems?>, t: Throwable) {
                withIO {
                    mainRepository.submitErrorEvent(ErrorTypeOnNet.HotKeyErrorOnNet(t))
                }
            }
        })
        return successful
    }

    fun getCategoriesTitleOnNet() {
        val call: Call<CgBean>? = myAPIService.categories
        call!!.enqueue(object : Callback<CgBean?> {
            override fun onResponse(call: Call<CgBean?>, response: Response<CgBean?>) {
                if (response.isSuccessful) {
                    assert(response.body() != null)
                    val cgBean = response.body()!!
                    Log.d(TAG, "onResponse: ${cgBean.data}")
                    withIO {
                        mainRepository.submitCgBean(cgBean)
                    }
                }
            }
            override fun onFailure(call: Call<CgBean?>, t: Throwable) {
                withIO {
                    mainRepository.submitErrorEvent(ErrorTypeOnNet.CategoriesTitleAndItemErrorOnNet(t))
                }
            }
        })
    }

    fun getBannerDataOnNet() {
        val call: Call<BannerItems>? = myAPIService.bannerUrl
        call!!.enqueue(object : Callback<BannerItems> {
            override fun onResponse(call: Call<BannerItems>, response: Response<BannerItems>) {
                if (response.isSuccessful){
                    val list = response.body()
                    withIO {
                        if (list != null) {
                            withIO {
                                mainRepository.submitBannerItems(list)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<BannerItems?>, t: Throwable) {
                withIO {
                    mainRepository.submitErrorEvent(ErrorTypeOnNet.BannerOnNetError(t))
                }
            }
        })
    }

    fun getCategoriesData(id: Int, currentPage : Int) {

        val call = myAPIService.getCategoriesDetail(currentPage, id)
        Log.d(TAG + "id", id.toString() + "")
        Log.d(TAG + "page", currentPage.toString() + "")
        call!!.enqueue(object : Callback<NewItemBean?> {
            override fun onResponse(call: Call<NewItemBean?>, response: Response<NewItemBean?>) {
                if (response.isSuccessful) {

                    val newsData: NewItemBean = response.body()!!
//                    Log.d(TAG + "cg_connectSuccess", response.body()!!.data!!.datas.toString())
//                    Log.d(
//                        TAG + "cg_connectSuccessCurPage",
//                        "onResponse: " + response.body()!!.data!!.curpage
//                    )
                    withIO {
                        categoriesDetailRepository.submitCategoriesItemBean(newsData)
                    }

                }
            }

            override fun onFailure(call: Call<NewItemBean?>, t: Throwable) {
                Log.e(TAG + "connectFail", t.toString())
            }
        })
    }

    fun getSearchData(search: String?) {
        Log.d(TAG + "process_here_search)", "有執行到search")
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
    }




}