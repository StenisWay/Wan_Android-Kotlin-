package com.stenisway.wan_android.ui.search.hkbean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class HKItem {
    @PrimaryKey
    var id = 0

    @ColumnInfo(name = "name")
    var name: String? = null
}