package com.github.hongbeomi.flickrcodelab.ui.full_size

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.github.hongbeomi.flickrcodelab.R
import com.github.hongbeomi.flickrcodelab.databinding.FullSizeActivityBinding
import com.github.hongbeomi.flickrcodelab.ui.base.BaseActivity

class FullSizeActivity: BaseActivity<FullSizeActivityBinding>(R.layout.full_size_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra(KEY_URL_FULL_SIZE)
        url?.let {
            Glide.with(this)
                .load(it)
                .placeholder(ColorDrawable(Color.GRAY))
                .into(binding.imageViewFullSizeImage)
        }

        binding.imageButtonFullSizeClose.setOnClickListener {
            finish()
        }

    }

    companion object {
        const val KEY_URL_FULL_SIZE = "key_url_full_size"
    }

}