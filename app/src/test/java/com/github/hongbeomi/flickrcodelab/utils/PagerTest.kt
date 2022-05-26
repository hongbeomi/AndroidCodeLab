package com.github.hongbeomi.flickrcodelab.utils


import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

@ExperimentalCoroutinesApi
class PagerTest {

    private val pager: Pager = Pager()

    @Test
    fun startPageIndex_One() {
        assertThat(pager.currentPage, `is`(1))
    }

    @Test
    fun givenStartPageIndex_WhenLoadSuccess_ThenIncrementPageIndex() = runTest {
        assertThat(pager.currentPage, `is`(1))

        // when
        pager.load { true }

        // then
        assertThat(pager.currentPage, `is`(2))
    }

    @Test
    fun givenStartPageIndex_WhenLoadFail_ThenNotIncrementPageIndex() = runTest {
        assertThat(pager.currentPage, `is`(1))

        // when
        pager.load { false }

        // then
        assertThat(pager.currentPage, `is`(1))
    }

    @Test
    fun givenStartPageIndex_WhenLoadSuccessAndReset_ThenStartPageIndex() = runTest {
        assertThat(pager.currentPage, `is`(1))

        // when
        pager.load { true }
        pager.load { true }
        
        pager.reset()

        // then
        assertThat(pager.currentPage, `is`(1))
    }

}