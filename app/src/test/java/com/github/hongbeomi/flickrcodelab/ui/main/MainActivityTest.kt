package com.github.hongbeomi.flickrcodelab.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.hongbeomi.domain.EXCEPTION_MESSAGE_LIST_EMPTY
import com.github.hongbeomi.flickrcodelab.R
import com.github.hongbeomi.flickrcodelab.di.ServiceLocator
import com.github.hongbeomi.flickrcodelab.ui.full_size.FullSizeActivity
import com.github.hongbeomi.shared.fixtures.Fixtures
import com.github.hongbeomi.shared.repository.FakePhotoListRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private lateinit var repository: FakePhotoListRepository
    private val resources: Resources = ApplicationProvider.getApplicationContext<Context>().resources

    @Before
    fun setUp() {
        repository = FakePhotoListRepository()
        ServiceLocator.photoListRepository = repository
    }

    @After
    fun tearDown() {
        ServiceLocator.resetRepository()
        activityScenario.close()
    }

    @Test
    fun givenEmptyPhotoList_WhenStartMainPage_ThenShowLoadingAndEmptyError() {
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        // then
        onView(withId(R.id.progressBar_main)).check(matches(isDisplayed()))

        Robolectric.flushForegroundThreadScheduler()

        onView(withId(R.id.textView_main_error)).check(matches(isDisplayed()))
        onView(withId(R.id.textView_main_error)).check(
            matches(
                withText(
                    resources.getString(R.string.text_main_fetch_error, EXCEPTION_MESSAGE_LIST_EMPTY)
                )
            )
        )
    }

    @Test
    fun givenRemotePhotoList_WhenStartMainPage_ThenShowRemotePhotoList() {
        // given
        repository.addPhotoList(Fixtures.photo())

        // when
        activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // then
        onView(withId(R.id.progressBar_main)).check(matches(isDisplayed()))

        Robolectric.flushForegroundThreadScheduler()
        onView(withId(R.id.recyclerView_main)).check(matches(isDisplayed()))
    }

    @Test
    fun givenRemotePhotoList_WhenClickImage_ThenMoveDetail() {
        // given
        repository.addPhotoList(Fixtures.photo())

        // when
        activityScenario = ActivityScenario.launch(MainActivity::class.java)

        var activity : Activity? = null
        activityScenario.onActivity {
            activity = it
        }

        onView(withId(R.id.progressBar_main)).check(matches(isDisplayed()))

        Robolectric.flushForegroundThreadScheduler()
        onView(withId(R.id.recyclerView_main)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MainRecyclerAdapter.MainViewHolder>(0, ViewActions.click())
        )

        // then
        val expect = Intent(activity, FullSizeActivity::class.java)
        val actual = shadowOf(RuntimeEnvironment.getApplication()).nextStartedActivity

        assertThat(expect .component, `is`(actual.component))
    }

}