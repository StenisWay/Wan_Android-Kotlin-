package com.stenisway.wan_android.ui.favorite.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.stenisway.wan_android.ui.favorite.repo.FavoriteRepository
import com.stenisway.wan_android.util.roomutil.CRUDRepository
import com.stenisway.wan_android.util.roomutil.withIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteFragmentViewModel @Inject constructor(val repository: FavoriteRepository, val crudRepository: CRUDRepository) : ViewModel() {

    private val TAG = javaClass.simpleName
    fun getFavoriteList(){
        withIO {
            crudRepository.getFavoriteNewItems()
        }
    }
}