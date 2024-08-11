package com.stenisway.wan_android.ui.categories.categoriesbean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class CgTitle (
    @Ignore
    var children: List<CgItem>? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @PrimaryKey
    var id: Int = 0
)