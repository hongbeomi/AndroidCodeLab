package com.github.hongbeomi.flickrcodelab.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.github.hongbeomi.flickrcodelab.FlickrApplication
import com.github.hongbeomi.flickrcodelab.R
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private val repository by lazy { (application as FlickrApplication).photosRepository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}