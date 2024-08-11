package com.stenisway.wan_android.component.banner.bannerbean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class BannerItem {
    @PrimaryKey
    var id = 0

    @ColumnInfo(name = "imagePath")
    var imagePath: String = "無資料"
}
