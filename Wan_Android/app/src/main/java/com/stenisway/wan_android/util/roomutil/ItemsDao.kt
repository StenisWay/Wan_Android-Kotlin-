package com.stenisway.wan_android.util.roomutil

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.stenisway.wan_android.component.banner.bannerbean.BannerItem
import com.stenisway.wan_android.ui.categories.categoriesbean.CgItem
import com.stenisway.wan_android.ui.categories.categoriesbean.CgTitle
import com.stenisway.wan_android.ui.newitem.newsbean.NewItem
import com.stenisway.wan_android.ui.search.hkbean.HKItem

@Dao
interface ItemsDao {
    @Insert
    suspend fun insertNewItem(vararg newItems: NewItem)

    @Insert
    suspend fun insertBannerItem(vararg bannerItems: BannerItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHKItem(vararg hkItems: HKItem)

    @Insert
    suspend fun insertCgTitle(vararg cgTitles: CgTitle)

    @Insert
    suspend fun insertCgItem(vararg cgItems: CgItem)

    @Update
    suspend fun updateNewItem(vararg items: NewItem)

    @Delete
    suspend fun deleteNewItem(vararg items: NewItem)

    @Query(" DELETE FROM NewItem")
    suspend fun deleteAllNewItem()

    @Query("SELECT * FROM CgTitle")
    suspend fun allCgTitleLive(): List<CgTitle>

    @Query("SELECT * FROM CgItem")
    suspend fun allCgItemLive(): List<CgItem>

    @Query("SELECT * FROM NewItem")
    suspend fun allNewItemLive(): List<NewItem>

    @Query("SELECT * FROM NewItem WHERE id = :id")
    suspend fun getNewsItem(id: Int): NewItem

    @Query("SELECT * FROM NewItem WHERE favorite = 1")
    suspend fun allFavoriteNewItemLive(): List<NewItem>

    @Query("SELECT * FROM NewItem WHERE laterRead = 1")
    suspend fun allLaterReadNewItemLive(): List<NewItem>

    @Query("SELECT * FROM HKItem ORDER BY ID DESC")
    suspend fun allHKItemLive(): List<HKItem>

    @Query("SELECT * FROM BannerItem ORDER BY ID DESC")
    suspend fun allBannerItemLive(): List<BannerItem>

    @Query("SELECT * FROM NewItem WHERE laterRead = 0 and favorite = 0")
    suspend fun allDeleteItemLive(): List<NewItem>
}