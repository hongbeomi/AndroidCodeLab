package com.github.hongbeomi.flickrcodelab.model

import com.github.hongbeomi.domain.getImageUrl
import com.github.hongbeomi.fixtures.Fixtures
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class PhotoUtilsTest {

    @Test
    fun givenPhotoData_WhenGetImageUrl_ThenCorrectImageUrl() {
        // given
        val data = Fixtures.photo()

        // when
        val url = data.getImageUrl()

        // then
        assertThat(url, `is`("https://live.staticflickr.com/${data.server}/${data.id}_${data.secret}.jpg"))
    }

}