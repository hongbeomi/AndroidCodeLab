package com.github.hongbeomi.flickrcodelab.ui

import android.content.Context
import android.content.res.Resources
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.hongbeomi.domain.EXCEPTION_MESSAGE_LIST_EMPTY
import com.github.hongbeomi.flickrcodelab.DataBindingIdlingResource
import com.github.hongbeomi.flickrcodelab.R
import com.github.hongbeomi.flickrcodelab.di.ServiceLocator
import com.github.hongbeomi.flickrcodelab.monitorActivity
import com.github.hongbeomi.flickrcodelab.ui.main.MainActivity
import com.github.hongbeomi.flickrcodelab.ui.main.MainRecyclerAdapter
import com.github.hongbeomi.shared.EspressoIdlingResource
import com.github.hongbeomi.shared.repository.FakePhotoListRepository
import com.github.hongbeomi.shared.fixtures.Fixtures
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private lateinit var repository: FakePhotoListRepository

    private val resources: Resources =
        ApplicationProvider.getApplicationContext<Context>().resources
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setUp() {
        repository = FakePhotoListRepository()
        repository.setUseIdlingResource(true)
        ServiceLocator.photoListRepository = repository
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        activityScenario.close()
        ServiceLocator.resetRepository()
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun givenEmptyPhotoList_WhenStartMainPage_ThenShowLoadingAndEmptyError() {
        // when
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // then
        onView(withId(R.id.progressBar_main)).check(matches(isDisplayed()))

        onView(withId(R.id.textView_main_error)).check(matches(isDisplayed()))
        onView(withId(R.id.textView_main_error)).check(
            matches(
                withText(
                    resources.getString(
                        R.string.text_main_fetch_error, EXCEPTION_MESSAGE_LIST_EMPTY
                    )
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
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // then
        onView(withId(R.id.progressBar_main)).check(matches(isDisplayed()))

        onView(withId(R.id.recyclerView_main)).check(matches(isDisplayed()))
    }

    @Test
    fun givenRemotePhotoList_WhenClickImage_ThenMoveDetail() {
        // given
        repository.addPhotoList(Fixtures.photo())

        // when
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.progressBar_main)).check(matches(isDisplayed()))

        onView(withId(R.id.recyclerView_main)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MainRecyclerAdapter.MainViewHolder>(0, click())
        )

        // then
        onView(withId(R.id.imageView_full_size_image)).check(matches(isDisplayed()))
        onView(withId(R.id.imageButton_full_size_close)).check(matches(isDisplayed()))
    }

}