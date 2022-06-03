package com.github.hongbeomi.flickrcodelab

import com.github.hongbeomi.flickrcodelab.data.repository.DefaultPhotoListRepositoryTest
import com.github.hongbeomi.flickrcodelab.model.PhotoUtilsTest
import com.github.hongbeomi.flickrcodelab.ui.main.MainActivityTest
import com.github.hongbeomi.flickrcodelab.ui.main.MainViewModelTest
import com.github.hongbeomi.flickrcodelab.utils.PagerTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(Suite::class)
@Suite.SuiteClasses(
    MainActivityTest::class,
    MainViewModelTest::class,
    PhotoUtilsTest::class,
    PagerTest::class,
    DefaultPhotoListRepositoryTest::class
)
class UnitTestSuite