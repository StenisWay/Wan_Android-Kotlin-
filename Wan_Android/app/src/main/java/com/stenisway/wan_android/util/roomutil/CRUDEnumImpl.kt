package com.stenisway.wan_android.util.roomutil

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.stenisway.wan_android.component.banner.bannerbean.BannerItem
import com.stenisway.wan_android.ui.categories.categoriesbean.CgItem
import com.stenisway.wan_android.ui.categories.categoriesbean.CgTitle
import com.stenisway.wan_android.ui.newitem.newsbean.NewItem
import com.stenisway.wan_android.ui.search.hkbean.HKItem
import com.stenisway.wan_android.util.roomutil.CRUDEnum.*

/**
 * 不使用flow的原因
 * 1. 不會頻繁的傳遞資料到ui thread更新
 * 2. 使用flow會無法重複使用同一個查詢方法
 */
class CRUDEnumImpl(context: Context) {

    private val database: WanSqlLiteDatabase? = WanSqlLiteDatabase.getDatabase(context)
    private var dao: ItemsDao? = database?.itemDao

    fun dbClose() {
        database.let {
            it?.close()
        }
    }

    suspend fun useNewItemMethod(
        method: CRUDEnum,
        vararg bean: NewItem
    ): List<NewItem>? {
        if (dao == null) {
            return null
        }
        when (method) {
            CLEAR -> dao!!.deleteAllNewItem()
            DELETE -> dao!!.deleteNewItem(*bean)
            INSERT -> dao!!.insertNewItem(*bean)
            UPDATE -> dao!!.updateNewItem(*bean)
            SELECT_DELETE -> return dao!!.allDeleteItemLive()
            SELECT_ALL -> return dao!!.allNewItemLive()
            SELECT_LATER_READ -> return dao!!.allLaterReadNewItemLive()
            SELECT_FAVORITE -> return dao!!.allFavoriteNewItemLive()
        }
        return null
    }

    suspend fun selectById(id: Int): NewItem? {
        if (dao == null) {
            return null
        }
        return dao?.getNewsItem(id)
    }

    suspend fun useHKItemMethod(method: CRUDEnum, vararg bean: HKItem): List<HKItem>? {
        if (dao == null) {
            return null
        }
        return when (method) {
            CLEAR ->
                null

            DELETE ->
                null

            INSERT -> {
                dao!!.insertHKItem(*bean)
                null
            }

            SELECT_ALL -> dao!!.allHKItemLive()

            else -> {
                null
            }

        }
    }

    suspend fun useBannerItemMethod(
        method: CRUDEnum?,
        vararg bean: BannerItem
    ): List<BannerItem>? {
        if (dao == null) {
            return null
        }
        return when (method) {
            INSERT -> {
                dao!!.insertBannerItem(*bean)
                null
            }

            SELECT_ALL -> {
                dao!!.allBannerItemLive()
            }

            else -> {
                null
            }
        }
    }

    suspend fun useCgTitleMethod(method: CRUDEnum, vararg bean: CgTitle): List<CgTitle>? {
        if (dao == null) {
            return null
        }
        return when (method) {
            INSERT -> {
                dao!!.insertCgTitle(*bean)
                null
            }

            SELECT_ALL -> {
                dao!!.allCgTitleLive()
            }

            else -> {
                null
            }
        }
    }

    suspend fun useCgItemMethod(method: CRUDEnum, vararg bean: CgItem): List<CgItem>? {
        if (dao == null) {
            return null
        }
        return when (method) {
            INSERT -> {
                dao!!.insertCgItem(*bean)
                null
            }

            SELECT_ALL -> dao!!.allCgItemLive()

            else -> {
                null
            }
        }
    }


}