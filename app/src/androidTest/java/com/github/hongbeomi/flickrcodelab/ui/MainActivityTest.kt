package com.github.hongbeomi.flickrcodelab.ui

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import com.github.hongbeomi.flickrcodelab.DataBindingIdlingResource
import com.github.hongbeomi.flickrcodelab.R
import com.github.hongbeomi.flickrcodelab.data.source.FakePhotoDataFactory
import com.github.hongbeomi.flickrcodelab.data.source.FakePhotoListRepository
import com.github.hongbeomi.flickrcodelab.data.source.remote.EXCEPTION_MESSAGE_LIST_EMPTY
import com.github.hongbeomi.flickrcodelab.di.ServiceLocator
import com.github.hongbeomi.flickrcodelab.monitorActivity
import com.github.hongbeomi.flickrcodelab.utils.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private lateinit var repository: FakePhotoListRepository

    private val factory = FakePhotoDataFactory()

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
    fun tearDown() = runBlocking {
        ServiceLocator.resetRepository()
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        activityScenario.close()
    }

    @Test
    fun givenEmptyPhotoList_WhenStartMainPage_ThenShowLoadingAndEmptyError() = runBlocking<Unit> {
        // when
        activityScenario = ActivityScenario.launch(
            Intent(
                ApplicationProvider.getApplicationContext(),
                MainActivity::class.java
            )
        )
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
    fun givenRemotePhotoList_WhenStartMainPage_ThenShowRemotePhotoList() = runBlocking<Unit> {
        // given
        repository.addPhotoList(factory.photo1)

        // when
        activityScenario = ActivityScenario.launch(
            Intent(
                ApplicationProvider.getApplicationContext(),
                MainActivity::class.java
            )
        )
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // then
        onView(withId(R.id.progressBar_main)).check(matches(isDisplayed()))

        onView(withId(R.id.recyclerView_main)).check(matches(isDisplayed()))
    }

}