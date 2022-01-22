package com.openclassrooms.realestatemanager

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import com.openclassrooms.realestatemanager.activity.MainActivity

import androidx.test.rule.ActivityTestRule


/**
 * Created by Julien Jennequin on 21/01/2022 11:14
 * Project : RealEstateManager
 **/

@RunWith(AndroidJUnit4::class)
class UtilsTest {

    @Rule @JvmField
    public val mActivityRule = ActivityTestRule(MainActivity::class.java)


    @Test
    fun test_toast_wifi() {
        onView(withText(R.string.wifi_available)).inRoot(
            withDecorView(
                not(`is`(mActivityRule.activity.window.decorView))
            )
        ).check(
            matches(isDisplayed())
        )
    }
}