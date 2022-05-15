package com.github.hongbeomi.flickrcodelab.ui.main

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.github.hongbeomi.flickrcodelab.R
import com.github.hongbeomi.flickrcodelab.databinding.ItemMainBinding
import com.github.hongbeomi.flickrcodelab.model.Photo
import com.github.hongbeomi.flickrcodelab.model.getImageUrl

class MainRecyclerAdapter(
    private val glideRequestManager: RequestManager,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList: List<Photo> = listOf()

    fun submitList(value: List<Photo>) {
        itemList = value
        notifyItemRangeChanged(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_main -> MainViewHolder(
                DataBindingUtil.inflate<ItemMainBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_main,
                    parent,
                    false
                ).apply {
                    imageViewItemMain.layoutParams.height = parent.width / 3
                }
            )
            else -> FooterViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_loading_footer,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> R.layout.item_loading_footer
            else -> R.layout.item_main
        }
    }

    override fun getItemCount() = itemList.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList.getOrNull(position)
        if (holder is MainViewHolder && item != null) {
            holder.bind(item)
        }
    }

    inner class MainViewHolder(private val binding: ItemMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageViewItemMain.setOnClickListener {
                onItemClick.invoke(
                    itemList[adapterPosition].getImageUrl()
                )
            }
        }

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

    class FooterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}