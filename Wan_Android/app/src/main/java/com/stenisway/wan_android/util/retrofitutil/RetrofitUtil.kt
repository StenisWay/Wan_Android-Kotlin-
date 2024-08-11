package com.stenisway.wan_android.util.retrofitutil

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitUtil private constructor() {
    val api: MyAPIService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Api.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(MyAPIService::class.java)
    }

    companion object {
        val instance = RetrofitUtil()
    }
}