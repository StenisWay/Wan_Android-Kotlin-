package com.stenisway.wan_android.util.roomutil

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



fun withIO(func : suspend () -> Unit){
    val exception = CoroutineExceptionHandler{ _, e->
        e.printStackTrace()
    }
    CoroutineScope(Dispatchers.IO + exception).launch {
        func()
    }
}

