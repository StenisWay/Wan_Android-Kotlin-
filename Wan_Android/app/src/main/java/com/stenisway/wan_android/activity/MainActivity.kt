package com.stenisway.wan_android.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stenisway.wan_android.R
import com.stenisway.wan_android.base.ErrorTypeOnNet
import com.stenisway.wan_android.databinding.ActivityMainBinding
import com.stenisway.wan_android.ui.categories.categoriesbean.CgItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Objects

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.name
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setContentView(binding.root)
        initNav()
        getData()
        collectExecute()


    }

    private fun collectExecute() {
        //        在ViewModel執行collect容易有bug，所以在controller上執行
        lifecycleScope.launch(Dispatchers.IO) {

            if (viewModel.getBannerItemsFromLocal()) {
                viewModel.getBannerItemsFromNet()
                viewModel.bannerFlow.collect {
                    viewModel.saveBannerItems(it)
                }
            }
            viewModel.repository.hokeyItems.collect {
                viewModel.saveHokeyItems(it)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                if (!viewModel.getCategoriesItems()) {
                    viewModel.getCgBeanFromNet()
                    viewModel.cgBeanFlow.collect { cgBean ->
                        val cgTitles = cgBean.data
                        cgTitles?.let { viewModel.saveCategoriesTitle(it) }
                        val cgList = async {
                            val list = mutableListOf<CgItem>()
                            cgTitles?.forEach { cgtitle ->
                                val cgItem = cgtitle.children
                                cgItem?.forEach {
                                    list.add(it)
                                    Log.d(TAG, "CgItem $it")
                                }
                            }
                            list
                        }
                        viewModel.saveCategoriesItems(cgList.await())
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.repository.errorEvent.collect{
                Log.d(TAG, it.toString())
                delay(200)
                when(it){
                    is ErrorTypeOnNet.NewItemErrorOnNet ->{
                        viewModel.retrofitRequest.getNewsData(it.page)
                    }
                    is ErrorTypeOnNet.BannerOnNetError -> {
                        viewModel.retrofitRequest.getBannerDataOnNet()
                    }
                    is ErrorTypeOnNet.CategoriesDataErrorOnNet -> {
                        viewModel.retrofitRequest.getCategoriesData(it.id, it.page)
                    }
                    is ErrorTypeOnNet.CategoriesTitleAndItemErrorOnNet -> {
                        viewModel.retrofitRequest.getCategoriesTitleOnNet()
                    }
                    is ErrorTypeOnNet.HotKeyErrorOnNet -> {
                        viewModel.retrofitRequest.getHkOnNet()
                    }


                }

            }
        }
    }

    private fun initView() {
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    private fun initNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener {item ->
            when(item.itemId){
                R.id.newsFragment -> {
                    navController.navigate(R.id.newsFragment)
                }
                R.id.categoriesFragment -> {
                    navController.navigate(R.id.categoriesFragment)
                }
                R.id.settingFragment -> {
                    navController.navigate(R.id.settingFragment)
                }
                R.id.favoriteFragment -> {
                    navController.navigate(R.id.favoriteFragment)
                }
            }
            return@setOnItemSelectedListener true
        }
//        setupWithNavController(bottomNavigationView, navController)
    }

    private fun getData() {
        viewModel.getHokeyItemsOnNet()
    }

    fun changeTitle(stringId: Int) {
        Objects.requireNonNull(supportActionBar)?.title =
            getString(stringId)
    }
}