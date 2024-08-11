package com.stenisway.wan_android.ui.favorite.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.stenisway.wan_android.ui.favorite.repo.LaterReadRepository
import com.stenisway.wan_android.util.roomutil.CRUDRepository
import com.stenisway.wan_android.util.roomutil.withIO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaterReadViewModel @Inject constructor(private val repository: LaterReadRepository, private val crudRepository: CRUDRepository) : ViewModel() {


    val laterReadItemsFlow = repository.laterReadItems

    fun getLaterReadItem(){
        withIO {
            crudRepository.getLaterReadNewItems()
        }
    }


}