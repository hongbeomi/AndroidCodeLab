package com.github.hongbeomi.flickrcodelab

import com.github.hongbeomi.flickrcodelab.data.source.local.FlickrDaoTest
import com.github.hongbeomi.flickrcodelab.data.source.local.PhotoListLocalDataSourceTest
import com.github.hongbeomi.flickrcodelab.ui.MainActivityTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.runner.RunWith
import org.junit.runners.Suite

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@RunWith(Suite::class)
@Suite.SuiteClasses(
    MainActivityTest::class,
    FlickrDaoTest::class,
    PhotoListLocalDataSourceTest::class
)
class UiTestSuite