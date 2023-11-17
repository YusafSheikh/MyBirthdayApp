package com.example.mybirthdayapp

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.mybirthdayapp", appContext.packageName)
    }

    @Test
    fun TestButtons() {
        fun testButtons() {
        //    val scenario: FragmentScenario<SecondFragment> = launchFragmentInContainer  (
        //        themeResId = R.style.Theme_MyBirthdayApp
     //       )
            onView(withId(R.id.button_filter_name)).perform(click())

            onView(withId(R.id.button_sort)).perform(click())
            Thread.sleep(1000) // Add a delay to wait for UI rendering

            onView(withId(R.id.fab)).perform(click())
        }

    }
}