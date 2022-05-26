package com.github.hongbeomi.flickrcodelab.model

import com.github.hongbeomi.flickrcodelab.data.source.FakePhotoDataFactory
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class PhotoUtilsTest {

    private val factory = FakePhotoDataFactory()

    @Test
    fun givenPhotoData_WhenGetImageUrl_ThenCorrectImageUrl() {
        // given
        val data = factory.photo1

        // when
        val url = data.getImageUrl()

        // then
        assertThat(url, `is`("https://live.staticflickr.com/${data.server}/${data.id}_${data.secret}.jpg"))
    }

}