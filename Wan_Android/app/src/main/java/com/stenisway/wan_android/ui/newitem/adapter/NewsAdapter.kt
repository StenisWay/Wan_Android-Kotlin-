package com.stenisway.wan_android.ui.newitem.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stenisway.wan_android.R
import com.stenisway.wan_android.component.banner.BannerAdapter
import com.stenisway.wan_android.component.banner.bannerbean.BannerItem
import com.stenisway.wan_android.databinding.BannerItemBinding
import com.stenisway.wan_android.databinding.NewsFooterItemBinding
import com.stenisway.wan_android.databinding.NewsItemBinding
import com.stenisway.wan_android.ui.newitem.NewsDetailFragment
import com.stenisway.wan_android.ui.newitem.newsbean.NewItem
import com.stenisway.wan_android.util.StringUtil
import java.util.Timer
import java.util.TimerTask

class NewsAdapter : ListAdapter<NewItem, RecyclerView.ViewHolder?> {

    private val TAG = this.javaClass.name
    private val stringUtil: StringUtil
    private var pic_list: List<BannerItem>? = null
    private var bannerAdapter : BannerAdapter? = null
    private var noBanner = false
    private var noProgress = false

    fun setPic_list(pic_list: List<BannerItem>) {
        this.pic_list = pic_list
    }

    fun getPic_list() : List<BannerItem>?{
        return pic_list
    }

    fun getDataIsEmpty() : Boolean{
        return currentList.isEmpty()
    }

    constructor(noBanner: Boolean) : super(ItemCallBack()) {
        if (noBanner) {
            this.noBanner = true
        }
        stringUtil = StringUtil()
    }

    constructor(noBanner: Boolean, noProgress: Boolean) : super(ItemCallBack()) {
        if (noBanner) {
            this.noBanner = true
        }
        stringUtil = StringUtil()
        if (noProgress) {
            this.noProgress = true
        }
    }

    private val timer = Timer()

    //如果將timer放在BindViewHolder的區塊，會導致重複建構timer導致自動跳轉同時多次執行
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == NEWS_NORMAL_ITEM) {
            val binding: NewsItemBinding =
                NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemViewHolder(binding)
        }
        return if (viewType == News_Banner) {
            val binding: BannerItemBinding =
                BannerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val timerTask: TimerTask = object : TimerTask() {
                override fun run() {
                    Handler(Looper.getMainLooper()).post {
                        var currentPosition: Int = binding.vpBanner.currentItem
                        currentPosition++
                        binding.vpBanner.setCurrentItem(currentPosition, true)
                    }
                }
            }
            timer.schedule(timerTask, 5000, 5000)
            //           (執行重複的區塊, 執行程式碼時先等候多久在開始, 之後每次執行的間隔時間)
            BannerViewHolder(binding)
        } else {
            val binding: NewsFooterItemBinding =
                NewsFooterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (!noBanner) {
            if (position == 0) {
                return News_Banner
            }
        }
        if (!noProgress) {
            if (position == itemCount - 1) {
                return NEWS_FOOTER_ITEM
            }
        }
        return NEWS_NORMAL_ITEM
    }

    override fun getItemCount(): Int {
        var size = super.getItemCount() + 2
        if (noBanner) {
            size--
        }
        if (noProgress) {
            size--
        }
        return size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {

//        有Banner會多第一行
        var _position = position
        if (!noBanner) {
//            Log.d(TAG + "position before", _position.toString() + "")
            _position -= 1
//            Log.d(TAG + "position after", _position.toString() + "")
        }
        if (holder.itemViewType == NEWS_NORMAL_ITEM) {
//            取出物件，做優化
            val item: NewItem? = getItem(_position)
            val myViewHolder = holder as ItemViewHolder
            val title = item?.title?.let { stringUtil.replaceInvalidChar(it).trim { it <= ' ' } }
            myViewHolder.binding.txtTitle.text = title
            myViewHolder.binding.txtTime.text = item?.niceDate
            myViewHolder.binding.txtChapterName.text = item?.chapterName
            myViewHolder.binding.txtAuthor.text = item?.author
            item?.title?.let { Log.d(TAG, it) }
            myViewHolder.itemView.setOnClickListener { view: View ->
                val bundle = Bundle()
                bundle.putSerializable("newItemUrl", item)
                val navController = view.findNavController()
                navController.navigate(R.id.newsDetailFragment, bundle)
            }
        } else if (holder.itemViewType == News_Banner) {
            val bannerViewHolder = holder as BannerViewHolder
            if (pic_list != null) {
                bannerAdapter = BannerAdapter(pic_list!!)
                bannerViewHolder.binding.vpBanner.adapter = bannerAdapter
                bannerViewHolder.binding.vpBanner.currentItem = 500
            }
        } else {
            val myViewHolder2 = holder as LoadingViewHolder
            if (noProgress || itemCount < 5) {
                holder.binding.root.visibility = View.GONE
            } else {
                holder.binding.root.visibility = View.VISIBLE
                myViewHolder2.binding.txtLodingResult.setText(R.string.loading)
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        timer.cancel()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    private class ItemViewHolder(binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: NewsItemBinding

        init {
            this.binding = binding
        }
    }

    private class LoadingViewHolder(binding: NewsFooterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: NewsFooterItemBinding

        init {
            this.binding = binding
        }
    }

    private class BannerViewHolder(binding: BannerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: BannerItemBinding

        init {
            this.binding = binding
        }
    }

    private class ItemCallBack : DiffUtil.ItemCallback<NewItem>() {
        override fun areItemsTheSame(oldItem: NewItem, newItem: NewItem): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: NewItem, newItem: NewItem): Boolean {
            return oldItem === newItem
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
    }

    companion object {
        const val NEWS_NORMAL_ITEM = 0x1
        const val NEWS_FOOTER_ITEM = 0x2
        const val News_Banner = 0x3
    }
}
