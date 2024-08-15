package com.stenisway.wan_android.ui.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.stenisway.wan_android.R
import com.stenisway.wan_android.base.BaseFragment
import com.stenisway.wan_android.databinding.FragmentCategoriesBinding
import com.stenisway.wan_android.ui.categories.adapter.CategoriesAdapter
import com.stenisway.wan_android.ui.categories.viewmodel.CategoriesViewModel
import com.stenisway.wan_android.util.getScreenWidthAndHeight
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoriesFragment : BaseFragment() {
    private lateinit var viewModel: CategoriesViewModel
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var adapter: CategoriesAdapter
    private val TAG = javaClass.simpleName
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[CategoriesViewModel::class.java]
        binding = FragmentCategoriesBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCategoriesFromLocal()
        adapter  = CategoriesAdapter()
        binding.rvCglist.adapter = adapter
        binding.rvCglist.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            viewModel.repository.cgItemsFlow.collect{cgItems ->
                Log.d(TAG, "cgItemCollect: $cgItems")
                viewModel.repository.cgTitlesFlow.collect{cgTitles ->
                    Log.d(TAG, "cgTitleCollect: $cgTitles")
                    if (!adapter.getIsHavingData()){
                        adapter.setData(cgTitles, cgItems)
                    }
                }
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        changeTitle(R.string.Component)
    }

}