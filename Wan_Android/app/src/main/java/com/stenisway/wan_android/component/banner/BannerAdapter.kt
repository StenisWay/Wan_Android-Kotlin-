package com.stenisway.wan_android.component.banner

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stenisway.wan_android.component.banner.bannerbean.BannerItem
import com.stenisway.wan_android.databinding.BannerPictureBinding

class BannerAdapter(picList: List<BannerItem>) :
    RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
    private val picList: List<BannerItem>
    private val TAG = this.javaClass.name

    init {
        this.picList = picList
        Log.d(TAG, "piclist_items: $picList")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding: BannerPictureBinding =
            BannerPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        if (picList.isNotEmpty()) {
            val i = position % picList.size
//            Log.d(TAG + "picUrl", picList[i].imagePath)
            Glide.with(holder.binding.root)
                .load(Uri.parse(picList[i].imagePath))
                .into(holder.binding.bannerPicture)
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    inner class BannerViewHolder(binding: BannerPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: BannerPictureBinding

        init {
            this.binding = binding
        }
    }
}