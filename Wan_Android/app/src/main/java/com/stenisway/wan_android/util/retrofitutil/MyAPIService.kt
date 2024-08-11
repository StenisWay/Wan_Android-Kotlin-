package com.stenisway.wan_android.util.retrofitutil

import com.stenisway.wan_android.component.banner.bannerbean.BannerItems
import com.stenisway.wan_android.ui.categories.categoriesbean.CgBean
import com.stenisway.wan_android.ui.newitem.newsbean.NewItemBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import com.stenisway.wan_android.ui.search.hkbean.HokeyItems

interface MyAPIService {
    @GET(Api.NewsUrl + "/{page}/json?page_size=20")
    fun getNewUrl(@Path("page") page: Int): Call<NewItemBean>?

    @get:GET(Api.BannerUrl)
    val bannerUrl: Call<BannerItems>?

    @get:GET(Api.HotKeys)
    val hotKeys: Call<HokeyItems>?

    @POST(Api.Search + "/{page}/json")
    fun getSearchUrl(@Path("page") page: Int, @Query("k") searchTitle: String?): Call<NewItemBean>?

    @get:GET(Api.Categories)
    val categories: Call<CgBean>?

    @get:GET(Api.Categories)
    val categoriesD: CgBean?

    @GET(Api.CategoriesDetail + "/{page}/json")
    fun getCategoriesDetail(@Path("page") page: Int, @Query("cid") id: Int): Call<NewItemBean>?
}