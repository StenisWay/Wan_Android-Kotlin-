package com.stenisway.wan_android.util.roomutil

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.stenisway.wan_android.component.banner.bannerbean.BannerItem
import com.stenisway.wan_android.ui.categories.categoriesbean.CgTitle
import com.stenisway.wan_android.ui.categories.categoriesbean.CgItem
import com.stenisway.wan_android.ui.newitem.newsbean.NewItem
import com.stenisway.wan_android.ui.search.hkbean.HKItem

@Database(
    entities = [NewItem::class, BannerItem::class, HKItem::class, CgItem::class, CgTitle::class],
    version = 1,
    exportSchema = false
)
abstract class WanSqlLiteDatabase : RoomDatabase() {
    abstract val itemDao: ItemsDao?

    companion object {
        private var INSTANCE: WanSqlLiteDatabase? = null
        @Synchronized
        fun getDatabase(context: Context): WanSqlLiteDatabase? {
            if (INSTANCE == null) {
                INSTANCE = databaseBuilder(
                    context.applicationContext,
                    WanSqlLiteDatabase::class.java, "wanAndroid_database"
                ) //                    .addMigrations()
                    .build()
            }
            return INSTANCE
        }
    }
}