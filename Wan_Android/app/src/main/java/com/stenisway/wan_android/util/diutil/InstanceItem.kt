package com.stenisway.wan_android.util.diutil

import android.content.Context
import com.stenisway.wan_android.activity.MainActivityRepository
import com.stenisway.wan_android.ui.categories.repo.CategoriesDetailRepository
import com.stenisway.wan_android.ui.categories.repo.CategoriesRepository
import com.stenisway.wan_android.ui.favorite.repo.FavoriteRepository
import com.stenisway.wan_android.ui.favorite.repo.LaterReadRepository
import com.stenisway.wan_android.ui.newitem.repo.NewsDetailRepository
import com.stenisway.wan_android.ui.newitem.repo.NewsRepository
import com.stenisway.wan_android.ui.search.SearchRepository
import com.stenisway.wan_android.util.retrofitutil.RetrofitRequest
import com.stenisway.wan_android.util.roomutil.CRUDEnumImpl
import com.stenisway.wan_android.util.roomutil.CRUDRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class InstanceItem{

    @Singleton
    @Provides
    fun provideCRUD(@ApplicationContext context: Context) = CRUDEnumImpl(context)

    @Singleton
    @Provides
    fun provideRetrofitRepository(@ApplicationContext context: Context) = RetrofitRequest(context)

    @Singleton
    @Provides
    fun provideNewsRepository() = NewsRepository()

    @Singleton
    @Provides
    fun provideNewsDetailRepository() = NewsDetailRepository()

    @Singleton
    @Provides
    fun provideSearchView() = SearchRepository()

    @Singleton
    @Provides
    fun provideFavoriteRepository() = FavoriteRepository()

    @Singleton
    @Provides
    fun provideLaterReadRepository() = LaterReadRepository()

    @Singleton
    @Provides
    fun provideCategoriesRepository() = CategoriesRepository()

    @Singleton
    @Provides
    fun provideCategoriesDetailRepository() = CategoriesDetailRepository()

    @Singleton
    @Provides
    fun provideCRUDItems(@ApplicationContext context: Context) = CRUDRepository(context)

    @Singleton
    @Provides
    fun provideMainActivityRepository() = MainActivityRepository()

}