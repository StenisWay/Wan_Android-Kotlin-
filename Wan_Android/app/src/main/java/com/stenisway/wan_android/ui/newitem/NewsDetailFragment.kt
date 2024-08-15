package com.stenisway.wan_android.ui.newitem

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.just.agentweb.AgentWeb
import com.stenisway.wan_android.R
import com.stenisway.wan_android.base.BaseFragment
import com.stenisway.wan_android.databinding.FragmentNewsDetailBinding
import com.stenisway.wan_android.ui.newitem.newsbean.NewItem
import com.stenisway.wan_android.ui.newitem.viewmodel.NewsDetailViewModel
import com.stenisway.wan_android.util.roomutil.withIO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsDetailFragment : BaseFragment() {
    private lateinit var viewModel: NewsDetailViewModel
    private lateinit var binding: FragmentNewsDetailBinding
    private lateinit var mAgentWeb: AgentWeb
    private val TAG = javaClass.simpleName
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailBinding.inflate(
            LayoutInflater.from(requireContext()), container, false
        )
        viewModel = ViewModelProvider(this)[NewsDetailViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getItemFromArg()
        setWebViewAndActionBar()
//      LiveData同步收到newItem時，再來執行webView的配置，以及actionBar的配置

    }

    private fun setWebViewAndActionBar(){
        viewModel.newItem.observe(viewLifecycleOwner) { new ->
            mAgentWeb = AgentWeb.with(requireActivity()).setAgentWebParent(
                binding.webViewContainer, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
            ).useDefaultIndicator().createAgentWeb().ready().go(new.link)
            lifecycleScope.launch {
                withIO {
                    viewModel.getLocalItem(new)
                }
            }
            lifecycleScope.launch {
                viewModel.repository.localItem.collect {
                    Log.d(TAG, "getNewItems")
                    new.laterRead = it.laterRead
                    new.favorite = it.favorite
                }
            }
        }
        setmenu(requireActivity())
    }

    private fun getItemFromArg(){
        //        取得argument，並且阻塞線程使其不會還沒取得就先執行需要他的方法
        lifecycleScope.launch {
            val getItem : Deferred<NewItem?> =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    async {
                        arguments?.getSerializable("newItemUrl", NewItem::class.java)
                    }
                } else {
                    async {
                        arguments?.getSerializable("newItemUrl") as NewItem
                    }
                }
            val newItem = getItem.await()
            newItem?.let {
                viewModel.setNetItem(newItem)
            }
        }
    }


    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroyView() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroyView()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        changeTitle(R.string.content)
    }

    private fun setItemViewInitStatus(menu: Menu, newItem: NewItem) {
        val itemFavorite = menu.findItem(R.id.menu_favortie)
        val itemLaterRead = menu.findItem(R.id.menu_later)

        if (newItem.favorite) {
            itemFavorite.setIcon(R.drawable.baseline_favorite_red_24)
        } else {
            itemFavorite.setIcon(R.drawable.baseline_favorite_white_24)
        }

        if (newItem.laterRead) {
            itemLaterRead.setIcon(R.drawable.baseline_watch_later_orange_24)
        } else {
            itemLaterRead.setIcon(R.drawable.baseline_watch_later_white_24)
        }
    }

    private fun setmenu(menuHost: MenuHost) {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_menu, menu)
//                val itemSearch = menu.findItem(R.id.app_bar_search)
//                itemSearch.isVisible = false
                viewModel.newItem.value?.let { setItemViewInitStatus(menu, it) }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_favortie -> if (viewModel.newItem.value?.favorite == true) {
                        viewModel.newItem.value?.favorite = false
                        menuItem.setIcon(R.drawable.baseline_favorite_white_24)
                        viewModel.newItem.value?.let { viewModel.updateNewItem(it) }
                    } else {
                        menuItem.setIcon(R.drawable.baseline_favorite_red_24)
                        viewModel.newItem.value?.favorite = true
                        viewModel.newItem.value?.let { viewModel.updateNewItem(it) }
                    }

                    R.id.menu_later -> if (viewModel.newItem.value?.laterRead == true) {
                        menuItem.setIcon(R.drawable.baseline_watch_later_white_24)
                        viewModel.newItem.value?.laterRead = false
                        viewModel.newItem.value?.let {
                            viewModel.updateNewItem(it)
                        }
                    } else {
                        menuItem.setIcon(R.drawable.baseline_watch_later_orange_24)
                        viewModel.newItem.value?.laterRead = true
                        viewModel.newItem.value?.let { viewModel.updateNewItem(it) }

                    }
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


}