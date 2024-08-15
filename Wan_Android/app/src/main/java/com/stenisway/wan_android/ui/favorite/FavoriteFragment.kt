package com.stenisway.wan_android.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.stenisway.wan_android.databinding.FragmentFavoriteBinding
import com.stenisway.wan_android.ui.favorite.viewmodel.FavoriteFragmentViewModel
import com.stenisway.wan_android.ui.newitem.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var viewModel: FavoriteFragmentViewModel
    private lateinit var binding: FragmentFavoriteBinding
    private val TAG = javaClass.simpleName
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[FavoriteFragmentViewModel::class.java]
        binding =
            FragmentFavoriteBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = NewsAdapter(noBanner = true, noProgress = true)
        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {

            viewModel.repository.favoriteItems.collect{
                adapter.submitList(it).also {
                    binding.rvFavorite.scrollToPosition(0)
                }
            }
        }
    }

    override fun onResume() {
        viewModel.getFavoriteList()
        super.onResume()
    }
}