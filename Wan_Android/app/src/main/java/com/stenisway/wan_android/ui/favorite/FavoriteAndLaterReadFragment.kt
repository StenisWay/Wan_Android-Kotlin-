package com.stenisway.wan_android.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.stenisway.wan_android.R
import com.stenisway.wan_android.activity.MainActivity
import com.stenisway.wan_android.databinding.FragmentFavoriteLaterReadBinding
import com.stenisway.wan_android.ui.favorite.viewmodel.FavoriteAndLaterReadViewModel

class FavoriteAndLaterReadFragment : Fragment() {
    private lateinit var viewModel: FavoriteAndLaterReadViewModel
    private lateinit var binding: FragmentFavoriteLaterReadBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteLaterReadBinding.inflate(LayoutInflater.from(requireContext()))
        viewModel = ViewModelProvider(this)[FavoriteAndLaterReadViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.tabTitles = (
            listOf(
                getString(R.string.favorite_article),
                getString(R.string.latter_read)
            )
        )
        binding.vpFavorite.adapter = object :
            FragmentStateAdapter(childFragmentManager, lifecycle) {
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> FavoriteFragment()
                    1 -> LaterReadFragment()
                    else -> FavoriteFragment()
                }

            }

            override fun getItemCount(): Int {
                return viewModel.tabTitles!!.size
            }
        }

        binding.vpFavorite.registerOnPageChangeCallback(object : OnPageChangeCallback() {})
        TabLayoutMediator(
            binding.tlFavorite,
            binding.vpFavorite
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = viewModel.tabTitles!![position]
        }.attach()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val activity = requireActivity() as MainActivity
        activity.changeTitle(R.string.favorite_article)
    }

    companion object {
        val instance: FavoriteAndLaterReadFragment
            get() = FavoriteAndLaterReadFragment()
    }
}