package com.stenisway.wan_android.util.roomutil

import android.content.Context
import android.util.Log
import com.stenisway.wan_android.activity.MainActivityRepository
import com.stenisway.wan_android.base.ErrorEventOnLocal
import com.stenisway.wan_android.component.banner.bannerbean.BannerItems
import com.stenisway.wan_android.ui.categories.categoriesbean.CgItem
import com.stenisway.wan_android.ui.categories.categoriesbean.CgTitle
import com.stenisway.wan_android.ui.categories.repo.CategoriesRepository
import com.stenisway.wan_android.ui.favorite.repo.FavoriteRepository
import com.stenisway.wan_android.ui.favorite.repo.LaterReadRepository
import com.stenisway.wan_android.ui.newitem.newsbean.NewItem
import com.stenisway.wan_android.ui.newitem.repo.NewsDetailRepository
import com.stenisway.wan_android.ui.newitem.repo.NewsRepository
import com.stenisway.wan_android.ui.search.hkbean.HokeyItems
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class CRUDRepository(context: Context) {

    private val TAG = javaClass.simpleName

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface RepositoryEntryPoint {
        fun productCRUDEnumImpl(): CRUDEnumImpl
        fun newsDetailRepository(): NewsDetailRepository
        fun newsRepository(): NewsRepository
        fun mainActivityRepository(): MainActivityRepository
        fun categoriesRepository(): CategoriesRepository
        fun favoriteRepository() : FavoriteRepository
        fun laterReadRepository() : LaterReadRepository
    }

    private val appContext = context.applicationContext ?: throw IllegalStateException()
    private val hiltEntryPoint =
        EntryPointAccessors.fromApplication(appContext, RepositoryEntryPoint::class.java)
    private val crudEnumImpl = hiltEntryPoint.productCRUDEnumImpl()
    private val newsDetailRepository = hiltEntryPoint.newsDetailRepository()
    private val newsRepository = hiltEntryPoint.newsRepository()
    private val categoriesRepository = hiltEntryPoint.categoriesRepository()
    private val favoriteRepository = hiltEntryPoint.favoriteRepository()
    private val laterReadRepository = hiltEntryPoint.laterReadRepository()

    suspend fun getFavoriteNewItems(){
        withContext(Dispatchers.IO){
            val list = async {
                crudEnumImpl.useNewItemMethod(CRUDEnum.SELECT_FAVORITE)
            }
            Log.d(TAG, "getFavoriteNewItems: ${list.await()}")
            list.await()?.let {
                favoriteRepository.submitFavoriteItems(it)
            }
        }

    }

    suspend fun getLaterReadNewItems(){
        crudEnumImpl.useNewItemMethod(CRUDEnum.SELECT_LATER_READ)?.let {
            laterReadRepository.submitLaterReadItems(it)
        }
    }

    suspend fun getLocalItem(item: NewItem) {
        withContext(Dispatchers.IO) {
            val job = async {
                if (crudEnumImpl.selectById(item.id) == null) {
                    crudEnumImpl.useNewItemMethod(CRUDEnum.INSERT, item)
                }
            }
            job.join()
            crudEnumImpl.selectById(item.id)?.let { newsDetailRepository.submitLocalItem(it) }
        }
    }

    suspend fun saveCategoriesItem(cgItems: List<CgItem>) {
        crudEnumImpl.useCgItemMethod(CRUDEnum.INSERT, *cgItems.toTypedArray())
    }

    suspend fun saveCategoriesTitle(cgTitles: List<CgTitle>) {
        crudEnumImpl.useCgTitleMethod(CRUDEnum.INSERT, *cgTitles.toTypedArray())
    }

    suspend fun getCategoriesItemFromLocalToCategoriesFragment() {
        withContext(Dispatchers.IO){
            val list = async {
                crudEnumImpl.useCgItemMethod(CRUDEnum.SELECT_ALL)
            }
            list.await()?.let {
                categoriesRepository.submitCgItems(it)
            }
        }
    }

    suspend fun getCategoriesItemFromLocalIsExist(): Boolean {
        return !crudEnumImpl.useCgItemMethod(CRUDEnum.SELECT_ALL).isNullOrEmpty()
    }

    suspend fun getCategoriesTitleFromLocalToCategoriesFragment() {
        withContext(Dispatchers.IO) {
            val list = async {
                crudEnumImpl.useCgTitleMethod(CRUDEnum.SELECT_ALL)
            }
            list.await()?.let {
                categoriesRepository.submitCgTitles(it)
            }
        }
    }


    suspend fun getBannerItemFromLocalToMainActivity(): Boolean {
        crudEnumImpl.useBannerItemMethod(CRUDEnum.SELECT_ALL).also {
            return it!!.isEmpty()
        }
    }


    suspend fun getBannerItemFromLocalToNewsFragment() {
        val bList = crudEnumImpl.useBannerItemMethod(CRUDEnum.SELECT_ALL)
        if (bList.isNullOrEmpty()){
            delay(200)
            newsRepository.submitErrorEvent(ErrorEventOnLocal.BannerOnLocalError)
        }else{
            newsRepository.submitBannerData(bList)
        }
    }

    suspend fun updateNewItem(newItem: NewItem) {
        crudEnumImpl.useNewItemMethod(CRUDEnum.UPDATE, newItem)
    }

    suspend fun saveBannerItems(bannerItems: BannerItems) {
        crudEnumImpl.useBannerItemMethod(CRUDEnum.INSERT, *bannerItems.data!!.toTypedArray())
    }

    suspend fun saveHokeyItems(hokeyItems: HokeyItems) {
        crudEnumImpl.useHKItemMethod(CRUDEnum.INSERT, *hokeyItems.data!!.toTypedArray())
    }

    suspend fun getHokeyItems() {
        crudEnumImpl.useHKItemMethod(CRUDEnum.SELECT_ALL)?.let {
        }
    }

}
