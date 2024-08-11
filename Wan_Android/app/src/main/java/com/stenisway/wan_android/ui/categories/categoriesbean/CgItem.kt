package com.stenisway.wan_android.ui.categories.categoriesbean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CgItem (

    @PrimaryKey
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "parentChapterId")
    var parentChapterId: Int = 0
)