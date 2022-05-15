package com.github.hongbeomi.flickrcodelab.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.hongbeomi.flickrcodelab.FlickrApplication
import com.github.hongbeomi.flickrcodelab.R
import com.github.hongbeomi.flickrcodelab.databinding.ActivityMainBinding
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@FlowPreview
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.inflate<ActivityMainBinding?>(
            layoutInflater,
            R.layout.activity_main,
            null,
            false
        ).apply {
            setContentView(this.root)
        }
    }
    private lateinit var viewModel: MainViewModel
    private val viewModelFactory by lazy {
        MainViewModelFactory((application as FlickrApplication).photoListRepository)
    }
    private val mainAdapter by lazy { MainRecyclerAdapter(Glide.with(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbarMain)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        with(binding) {
            lifecycleOwner = this@MainActivity
            vm = viewModel
            setUpRecyclerView(viewModel::setUiAction)
            setUpSwipeRefresh(viewModel::setUiAction)
        }

        with(viewModel) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    mainUiState.collect {
                        handleState(it)
                    }
                }
            }
        }
    }

    private fun ActivityMainBinding.setUpRecyclerView(loadMore: (MainViewModel.MainUiAction) -> Unit) {
        with(recyclerViewMain) {
            adapter = mainAdapter
            itemAnimator = null
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainActivity, 3).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (mainAdapter.getItemViewType(position)) {
                            R.layout.item_main -> 1
                            else -> 3
                        }
                    }
                }
            }
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    val layoutManager = (recyclerView.layoutManager as? GridLayoutManager) ?: return
                    val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()

                    if (lastVisiblePosition == mainAdapter.itemCount - 1) {
                        loadMore(MainViewModel.MainUiAction.LoadMore)
                    }
                }
            })
        }
    }

    private fun ActivityMainBinding.setUpSwipeRefresh(refresh: (MainViewModel.MainUiAction) -> Unit) {
        with(swipeRefreshLayoutMain) {
            setOnRefreshListener {
                refresh(MainViewModel.MainUiAction.Refresh(true))
                isRefreshing = false
            }
        }
    }

    private fun handleState(state: MainViewModel.MainUiState) {
        with(state) {
            if (this is MainViewModel.MainUiState.Success) {
                mainAdapter.submitList(photoList)
            } else if (this is MainViewModel.MainUiState.Error) {
                binding.textViewMainError.text = getString(
                    R.string.text_main_fetch_error,
                    exception.message
                )
            }
        }
    }

}