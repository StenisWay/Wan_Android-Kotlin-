package com.stenisway.wan_android.ui.newitem.newsbean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class NewItem : Serializable, Comparable<NewItem?> {
    @PrimaryKey
    var id = 0

    @ColumnInfo(name = "chapterName")
    var chapterName: String? = null

    @ColumnInfo(name = "link")
    var link: String? = null

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "author")
    var author: String? = null

    @ColumnInfo(name = "niceDate")
    var niceDate: String? = null

    @ColumnInfo(name = "favorite", defaultValue = "false")
    var favorite = false

    @ColumnInfo(name = "laterRead", defaultValue = "false")
    var laterRead = false

    @ColumnInfo(name = "publishTime")
    var publishTime = 0.0

    override fun compareTo(other: NewItem?): Int {
        return (other!!.publishTime - publishTime).toInt()
    }
}
