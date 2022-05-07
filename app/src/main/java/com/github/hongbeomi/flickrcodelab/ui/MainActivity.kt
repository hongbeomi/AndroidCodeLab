package com.github.hongbeomi.flickrcodelab.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.hongbeomi.flickrcodelab.FlickrApplication
import com.github.hongbeomi.flickrcodelab.R

class MainActivity : AppCompatActivity() {

    private val repository by lazy { (application as FlickrApplication).photosRepository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}