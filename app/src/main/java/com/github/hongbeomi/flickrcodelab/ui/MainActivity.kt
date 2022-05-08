package com.github.hongbeomi.flickrcodelab.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.github.hongbeomi.flickrcodelab.FlickrApplication
import com.github.hongbeomi.flickrcodelab.R
import com.github.hongbeomi.flickrcodelab.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.inflate(
            layoutInflater,
            R.layout.activity_main,
            null,
            false
        )
    }
    private lateinit var viewModel: MainViewModel
    private val mainAdapter by lazy { MainRecyclerAdapter(Glide.with(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = MainViewModelFactory(
            (application as FlickrApplication).photoListRepository
        ).create(MainViewModel::class.java)

        with(binding) {
            lifecycleOwner = this@MainActivity
            vm = viewModel
            setUpRecyclerView()
            setUpSwipeRefresh(viewModel::refresh)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.mainUiState.collect {
                    if (it is MainViewModel.MainUiState.Success) {
                        mainAdapter.submitList(it.photoList)
                    }
                    if (it is MainViewModel.MainUiState.Error) {
                        binding.textViewMainError.text = getString(
                            R.string.text_main_fetch_error,
                            it.exception.message
                        )
                    }
                }
            }
        }
    }

    private fun ActivityMainBinding.setUpRecyclerView() {
        with(recyclerViewMain) {
            adapter = mainAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 3)
        }
    }

    private fun ActivityMainBinding.setUpSwipeRefresh(refresh: (Boolean) -> Unit) {
        with(swipeRefreshLayoutMain) {
            setOnRefreshListener {
                refresh(true)
                isRefreshing = false
            }
        }
    }

}