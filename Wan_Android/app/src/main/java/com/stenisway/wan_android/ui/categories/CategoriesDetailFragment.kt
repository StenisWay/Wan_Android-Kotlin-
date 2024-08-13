package com.stenisway.wan_android.ui.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stenisway.wan_android.base.BaseFragment
import com.stenisway.wan_android.databinding.FragmentCategoriesDetailBinding
import com.stenisway.wan_android.ui.categories.viewmodel.CategoriesDetailViewModel
import com.stenisway.wan_android.ui.newitem.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoriesDetailFragment : BaseFragment() {
    private lateinit var viewModel: CategoriesDetailViewModel
    private lateinit var binding: FragmentCategoriesDetailBinding
    private val TAG = this.javaClass.name
    private var adapter: NewsAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[CategoriesDetailViewModel::class.java]
        binding = FragmentCategoriesDetailBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val id = async {
                arguments?.getInt("id")
            }

            id.await()?.let {
                viewModel.getCategoriesData(it)
                viewModel.id = it
            }
        }

        adapter = NewsAdapter(true)
        binding.rvCategories.adapter = adapter
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewModel.categoriesDetailRepository.categoriesItemBeanFlow.collectLatest { newItemBean ->
                newItemBean.data?.let { NewItems ->
                    viewModel.page.ISLOADING = false
                    viewModel.page.TOTAL_PAGE = NewItems.pageCount
                    if (viewModel.page.needToScrollToTop){
                        viewModel.clearData()
                    }
                    adapter?.submitList(viewModel.getAllData(NewItems.datas))
                    if (viewModel.page.needToScrollToTop) {
                        binding.rvCategories.scrollToPosition(0)
                        viewModel.page.needToScrollToTop = false
                    }
                }
            }
        }

        binding.rvCategories.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.d(TAG + "newItemChange", "onScrollStateChanged: $newState")
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0) return
                val manager = (binding.rvCategories.layoutManager as LinearLayoutManager)
                val position = manager.findLastVisibleItemPosition()
                super.onScrolled(recyclerView, dx, dy)
                if (!viewModel.page.needToScrollToTop) {
                    if (position == adapter!!.itemCount - 1) {
                        viewModel.getCategoriesData()
                        Log.d(TAG + "到底讀取資料", "讀取成功")
                    }
                    if (viewModel.page.isOverPage()) {
                        kotlin.runCatching {
                            val lastItem = manager.findViewByPosition(adapter!!.itemCount - 1)!!
                            lastItem.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        })
    }

    private fun setBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        if (viewModel.dataIsEmpty()){
            viewModel.getCategoriesData()
        }
        super.onResume()
    }

    override fun onPause() {
        viewModel.page.needToScrollToTop = true
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

}