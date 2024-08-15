package com.stenisway.wan_android.ui.categories.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat.getDisplay
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.stenisway.wan_android.R
import com.stenisway.wan_android.databinding.CategoriesItemBinding
import com.stenisway.wan_android.ui.categories.categoriesbean.CgItem
import com.stenisway.wan_android.ui.categories.categoriesbean.CgTitle


class CategoriesAdapter() :
    RecyclerView.Adapter<CategoriesAdapter.CgViewHolder>() {
    private var cgTitles: List<CgTitle> = emptyList()
    private var cgItems: List<CgItem> = emptyList()
    private val TAG = this.javaClass.name
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CgViewHolder {
        val binding: CategoriesItemBinding =
            CategoriesItemBinding.inflate(LayoutInflater.from(parent.context))
        return CgViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(cgTitles: List<CgTitle>, cgItems: List<CgItem>){
        this.cgTitles = cgTitles
        this.cgItems = cgItems
        notifyDataSetChanged()
    }


    fun getIsHavingData() : Boolean{
        return cgTitles.isNotEmpty() && cgItems.isNotEmpty()
    }


    override fun onBindViewHolder(holder: CgViewHolder, position: Int) {
        val detail = cgTitles[position]
        holder.binding.txtCgtitle.text = detail.name
        holder.binding.cgLayout.removeAllViews()
        val itemSize = cgItems.size
        for (i in 0 until itemSize) {
            val item = cgItems[i]
            if (item.parentChapterId == detail.id) {
                val cgButton = createButton(holder.itemView.context, item)
                holder.binding.cgLayout.addView(cgButton)
            }
        }
    }

    private fun createButton(context: Context, item: CgItem): TextView {
        val button = AppCompatTextView(context).apply {
            text = item.name
            textSize = 16f
            setSingleLine()
            setPadding(0, 30, 0, 0)
            gravity = Gravity.CENTER
            setBackgroundResource(R.drawable.black_shap)
        }.apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val marginLayoutParams = MarginLayoutParams(layoutParams)
            marginLayoutParams.setMargins(30, 30, 30, 30)
            layoutParams = marginLayoutParams
//            Log.d(TAG + "buttonMargin", marginLayoutParams.leftMargin.toString() + "")
        }

        button.setOnClickListener { view: View ->
            val bundle = Bundle()
            bundle.putInt("id", item.id)
            Log.d("categories_itemID", item.id.toString() + "")
            val navController = view.findNavController()
            navController.navigate(R.id.action_categoriesFragment_to_categoriesDetailFragment, bundle)
        }
        return button
    }

    override fun getItemCount(): Int {
        return cgTitles.size
    }

    inner class CgViewHolder(binding: CategoriesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: CategoriesItemBinding

        init {
            this.binding = binding
        }
    }
}