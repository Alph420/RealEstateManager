package com.openclassrooms.realestatemanager

import android.view.KeyEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import com.openclassrooms.realestatemanager.view.activity.MainActivity
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep


/**
 * Created by Julien Jennequin on 21/01/2022 11:14
 * Project : RealEstateManager
 **/

@RunWith(AndroidJUnit4::class)
class UtilsTest {

    @Rule
    @JvmField
    val mActivityRule = ActivityTestRule(MainActivity::class.java)
    val device: UiDevice = UiDevice.getInstance(getInstrumentation())

    @Test
    fun test_toast_wifi() {
        device.openQuickSettings()
        if (UiObject(UiSelector().text("Airplane mode")).exists()) {
            UiObject(UiSelector().text("Airplane mode")).click()
            sleep(2000)
            device.pressBack()
            device.pressBack()
            sleep(2000)

            device.pressKeyCode(KeyEvent.KEYCODE_APP_SWITCH)
            device.pressKeyCode(KeyEvent.KEYCODE_APP_SWITCH)

            onView(withText(R.string.wifi_available)).inRoot(
                withDecorView(
                    not(`is`(mActivityRule.activity.window.decorView))
                )
            ).check(
                matches(isDisplayed())
            )
        } else {
            test_toast_wifi_not_available()
        }
    }

    @Test
    fun test_toast_wifi_not_available() {
        device.openQuickSettings()

        if (UiObject(UiSelector().text("Wi-Fi")).exists()) {
            UiObject(UiSelector().text("Wi-Fi")).click()
            sleep(2000)
            device.pressHome()

            device.pressKeyCode(KeyEvent.KEYCODE_APP_SWITCH)
            device.pressKeyCode(KeyEvent.KEYCODE_APP_SWITCH)

            onView(withText(R.string.wifi_not_available)).inRoot(
                withDecorView(
                    not(`is`(mActivityRule.activity.window.decorView))
                )
            ).check(
                matches(isDisplayed())
            )
        }
    }
}