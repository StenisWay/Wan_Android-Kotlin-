package com.stenisway.wan_android.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.stenisway.wan_android.databinding.FragmentLaterReadBinding
import com.stenisway.wan_android.ui.favorite.viewmodel.LaterReadViewModel
import com.stenisway.wan_android.ui.newitem.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class LaterReadFragment : Fragment() {

    private lateinit var viewModel: LaterReadViewModel

    private lateinit var binding: FragmentLaterReadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[LaterReadViewModel::class.java]
        binding = FragmentLaterReadBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = NewsAdapter(true, true)
        binding.rvLaterRead.adapter = adapter
        binding.rvLaterRead.layoutManager = LinearLayoutManager(requireContext())


        lifecycleScope.launch {
            viewModel.laterReadItemsFlow.collect{
                adapter.submitList(it).also {
                    binding.rvLaterRead.scrollToPosition(0)
                }
            }
        }
    }

    override fun onResume() {
        viewModel.getLaterReadItem()
        super.onResume()
    }
}
