package com.openclassrooms.realestatemanager

import android.content.Context
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
import com.openclassrooms.realestatemanager.view.activity.MainActivity

import androidx.test.rule.ActivityTestRule
import android.net.wifi.WifiManager





/**
 * Created by Julien Jennequin on 21/01/2022 11:14
 * Project : RealEstateManager
 **/

@RunWith(AndroidJUnit4::class)
class UtilsTest {

    @Rule @JvmField
    val mActivityRule = ActivityTestRule(MainActivity::class.java)


    @Test
    fun test_toast_wifi() {

        onView(withText(R.string.wifi_available)).inRoot(
            withDecorView(
                not(`is`(mActivityRule.activity.window.decorView))
            )
        ).check(
            matches(isDisplayed())
        )

        //TODO DISABLE WIFI
    }

    @Test
    fun test_toast_wifi_error() {
        var wifiStatus =  mActivityRule.activity.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiStatus.isWifiEnabled = false

        onView(withText(R.string.wifi_not_available)).inRoot(
            withDecorView(
                not(`is`(mActivityRule.activity.window.decorView))
            )
        ).check(
            matches(isDisplayed())
        )
    }
}