package com.stenisway.wan_android.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.stenisway.wan_android.component.banner.bannerbean.BannerItems
import com.stenisway.wan_android.ui.categories.categoriesbean.CgItem
import com.stenisway.wan_android.ui.categories.categoriesbean.CgTitle
import com.stenisway.wan_android.ui.search.hkbean.HokeyItems
import com.stenisway.wan_android.util.retrofitutil.RetrofitRequest
import com.stenisway.wan_android.util.roomutil.CRUDRepository
import com.stenisway.wan_android.util.roomutil.withIO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(application: Application, val crudRepository: CRUDRepository, val repository : MainActivityRepository, val retrofitRequest: RetrofitRequest) : AndroidViewModel(application) {

    val bannerFlow = repository.bannerFlow
    val cgBeanFlow = repository.cgFlow


    fun saveBannerItems(bannerItems: BannerItems){
        withIO {
            crudRepository.saveBannerItems(bannerItems)
        }
    }

    fun getCgBeanFromNet(){
        withIO {
            retrofitRequest.getCategoriesTitleOnNet()
        }
    }


    suspend fun getBannerItemsFromLocal() : Boolean{
         return crudRepository.getBannerItemFromLocalToMainActivity()
    }

    fun getHokeyItemsOnNet(){
        withIO {
            retrofitRequest.getHkOnNet()
        }
    }

    fun saveHokeyItems(hokeyItems: HokeyItems){
        withIO {
            crudRepository.saveHokeyItems(hokeyItems)
        }
    }


    fun getBannerItemsFromNet(){
        withIO {
            retrofitRequest.getBannerDataOnNet()
        }
    }

    suspend fun getCategoriesItems() : Boolean{
        return crudRepository.getCategoriesItemFromLocalIsExist()
    }

    fun saveCategoriesItems(cgItems: List<CgItem>){
        withIO {
            crudRepository.saveCategoriesItem(cgItems)
        }
    }

    fun saveCategoriesTitle(cgTitles: List<CgTitle>){
        withIO {
            crudRepository.saveCategoriesTitle(cgTitles)
        }
    }




}