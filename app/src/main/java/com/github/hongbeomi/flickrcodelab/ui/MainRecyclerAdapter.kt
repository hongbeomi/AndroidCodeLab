package com.github.hongbeomi.flickrcodelab.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.github.hongbeomi.flickrcodelab.R
import com.github.hongbeomi.flickrcodelab.databinding.ItemMainBinding
import com.github.hongbeomi.flickrcodelab.model.Photo
import com.github.hongbeomi.flickrcodelab.model.getImageUrl

class MainRecyclerAdapter(
    private val glideRequestManager: RequestManager
) : ListAdapter<Photo, MainRecyclerAdapter.MainViewHolder>(
    MainDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            DataBindingUtil.inflate<ItemMainBinding?>(
                LayoutInflater.from(parent.context),
                R.layout.item_main,
                parent,
                false
            ).apply {
                imageViewItemMain.layoutParams.height = parent.width / 3
            }
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MainViewHolder(
        private val binding: ItemMainBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            with(binding) {
                glideRequestManager
                    .load(photo.getImageUrl())
                    .placeholder(ColorDrawable(Color.GRAY))
                    .into(imageViewItemMain)
                executePendingBindings()
            }
        }
    }

}

class MainDiffCallback : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }
}