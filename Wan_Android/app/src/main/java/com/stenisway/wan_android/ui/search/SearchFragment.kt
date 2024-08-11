package com.stenisway.wan_android.ui.search

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stenisway.wan_android.R
import com.stenisway.wan_android.base.BaseFragment
import com.stenisway.wan_android.databinding.FragmentSearchBinding
import com.stenisway.wan_android.ui.newitem.adapter.NewsAdapter
import java.util.Objects

class SearchFragment : BaseFragment() {
    private lateinit var viewModel: SearchViewModel
    private val TAG = this.javaClass.name
    private lateinit var binding: FragmentSearchBinding
    private var keyWord: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(LayoutInflater.from(requireContext()))
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager: FragmentManager = getParentFragmentManager()
        manager.setFragmentResultListener(
            "searchItem",
            this,
            FragmentResultListener { requestKey: String?, result: Bundle ->
                keyWord = result.getString("searchText")
                viewModel.getSearchData(keyWord)
            })
        val searchNewsAdapter = NewsAdapter(true)
        binding.rvSearchList.setAdapter(searchNewsAdapter)
        binding.rvSearchList.setLayoutManager(LinearLayoutManager(requireContext()))
//        viewModel.getSearch().observe(getViewLifecycleOwner()) { items ->
//            if (viewModel.getSearch().getValue() == null || viewModel.getSearch().getValue()
//                    .isEmpty()
//            ) {
//                Handler().postDelayed({
//                    if (viewModel.getSearch().getValue() == null || viewModel.getSearch().getValue()
//                            .isEmpty()
//                    ) {
//                        binding.rvSearchList.setVisibility(View.GONE)
//                        binding.txtSearchNodata.setVisibility(View.VISIBLE)
//                    }
//                }, 200)
//            } else {
//                Log.d(
//                    "data",
//                    Objects.requireNonNull(viewModel.getSearch().getValue()).get(0)
//                        .getTitle()
//                )
//                binding.rvSearchList.setVisibility(View.VISIBLE)
//                binding.txtSearchNodata.setVisibility(View.GONE)
//                searchNewsAdapter.submitList(viewModel.getSearch().getValue())
//                if (viewModel.needToScrollToTop) {
//                    binding.rvSearchList.scrollToPosition(0)
//                    viewModel.needToScrollToTop = false
//                }
//            }
//        }
        binding.rvSearchList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.d(TAG + "SearchItemChange", "onScrollStateChanged: $newState")
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) return
                val manager = (binding.rvSearchList.getLayoutManager() as LinearLayoutManager)
                val position = manager.findLastVisibleItemPosition()
                if (position == searchNewsAdapter.itemCount - 1) {
                    viewModel.getSearchData(keyWord)
                }
            }
        })
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        changeTitle(R.string.search_result)
    }

    companion object {
        val instance: SearchFragment
            get() = SearchFragment()
    }
}