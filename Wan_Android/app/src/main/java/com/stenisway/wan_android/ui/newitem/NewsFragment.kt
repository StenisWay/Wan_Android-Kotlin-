package com.stenisway.wan_android.ui.newitem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stenisway.wan_android.R
import com.stenisway.wan_android.base.BaseFragment
import com.stenisway.wan_android.base.ErrorEventOnLocal
import com.stenisway.wan_android.databinding.FragmentNewsBinding
import com.stenisway.wan_android.ui.newitem.adapter.NewsAdapter
import com.stenisway.wan_android.ui.newitem.viewmodel.NewsViewModel
import com.stenisway.wan_android.util.roomutil.withIO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : BaseFragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    private val TAG = this.javaClass.name
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentNewsBinding.inflate(LayoutInflater.from(requireContext()))
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecycleView()
        getData()
    }

    private fun getData() {
        with(viewModel) {
            if (newsAdapter.currentList.isEmpty()){
                getBannerData()
                getNewsData()
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        changeTitle(R.string.News)
    }



    private fun setRecycleView() {

        initAdapter()
        collectRecycleViewData()
        setRecycleViewScrollListener()

    }

    private fun initAdapter() {
        newsAdapter = NewsAdapter(false)
        with(binding.recycleNews) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }
    }

    private fun setRecycleViewScrollListener() {
        binding.recycleNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.d(TAG + "newItemChange", "onScrollStateChanged: $newState")
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
//              判斷需要不要跳到position 0
                if (!viewModel.page.needToScrollToTop) {
                    if (dy < 0) return
                    val manager = (binding.recycleNews.layoutManager as LinearLayoutManager)
                    val position = manager.findLastVisibleItemPosition()
//                  判斷是最後的position時，抓取新的data
                    if (position == newsAdapter.itemCount - 1) {
                        viewModel.getNewsData()
                        Log.d(TAG + "NewItem execute loading?", "Yes")
//                        Log.d(
//                            TAG + "是否可以滑動",
//                            viewModel.page.needToScrollToTop.toString() + ""
//                        )
                    }
                }
            }
        })
    }

    private fun collectRecycleViewData() {
        //  若是先submitList在傳banner進去，會導致沒有圖片
        //  若是用notifyItemChanged(0)，畫面會自動跳轉到轉後一項(loading的那一個view也沒有顯示)，因為沒有滑動，所以無法使用滑動的指令跳回第一項
        //  取本地資料，速度比newsFlow快
        //      submitList的同時，要將banner的圖片放進去，並且滑到第一個item
        //      不然顯示的順序上會出問題
        //      以及有時候會沒辦法跳轉到第一個item

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.repository.bannerData.collect { banners ->
                viewModel.repository.newData.collectLatest { news ->
                    Log.d(TAG, "newItems data --> $news")
                    viewModel.page.TOTAL_PAGE = news.pageCount
                    viewModel.page.ISLOADING = false
                    Log.d(TAG, "NewItemsIsLoading: ${viewModel.page.ISLOADING}")
                    Log.d(TAG, "NewItemsTotalPage: ${viewModel.page.TOTAL_PAGE}")
                    hideProgress()
                    if (news.datas.isNotEmpty()){
                        Log.d(TAG, "FirstNewItemsData: ${news.datas[0].id}")
                        newsAdapter.submitList(viewModel.getAllData(news.datas))
                            .also {
                                if (viewModel.page.needToScrollToTop) {
                                    binding.recycleNews.scrollToPosition(0)
                                    viewModel.page.needToScrollToTop = false
                                }
                            }
                            .also {
                                if ((newsAdapter.getPic_list().isNullOrEmpty())) {
                                    Log.d(TAG, "bannerData --> $banners")
                                    if (banners.isEmpty()) {
                                        withIO {
                                            viewModel.getBannerData()

                                        }
                                    }
                                    if (viewModel.getBannerItem().isEmpty()) {
                                        viewModel.setBannerItem(banners)
                                    }
                                    newsAdapter.setPic_list(banners)
                                }
                            }
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.repository.errorEvent.collect{
                if (it is ErrorEventOnLocal.BannerOnLocalError){
                    viewModel.getBannerData()
                }
            }
        }
    }

    private fun hideProgress() {
        with(binding) {
            progressBar2.visibility = View.GONE
            txtNewsNodata.visibility = View.GONE
            recycleNews.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        viewModel.page.needToScrollToTop = true
        super.onDestroyView()
    }

}
