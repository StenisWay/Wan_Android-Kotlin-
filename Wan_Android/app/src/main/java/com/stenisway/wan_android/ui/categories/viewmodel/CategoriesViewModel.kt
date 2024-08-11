package com.stenisway.wan_android.ui.categories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stenisway.wan_android.ui.categories.repo.CategoriesRepository
import com.stenisway.wan_android.util.retrofitutil.RetrofitRequest
import com.stenisway.wan_android.util.roomutil.CRUDRepository
import com.stenisway.wan_android.util.roomutil.withIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(application: Application, val repository : CategoriesRepository, val crudEnumRepository: CRUDRepository, val retrofitRequest: RetrofitRequest) : AndroidViewModel(application) {

    fun getCategoriesFromLocal(){
        withIO {
            crudEnumRepository.getCategoriesTitleFromLocalToCategoriesFragment()
        }
        withIO {
            crudEnumRepository.getCategoriesItemFromLocalToCategoriesFragment()
        }
    }

}