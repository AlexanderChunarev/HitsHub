package com.example.hitshub

import android.view.MenuItem
import androidx.test.annotation.UiThreadTest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.hitshub.activities.MainActivity
import com.example.hitshub.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class TestBottomNavigationMenu {
    @get:Rule
    var mainActivity: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)
    private lateinit var bottomNavigationView: BottomNavigationView

    @Before
    fun setUp() {
        bottomNavigationView = mainActivity.activity.findViewById(R.id.nav_view)
    }

    @UiThreadTest
    @Test
    fun testNavigationViewSelectionItems() {
        val mockedListener = mock(BottomNavigationView.OnNavigationItemSelectedListener::class.java)
        bottomNavigationView.setOnNavigationItemSelectedListener(mockedListener)

        `when`(mockedListener.onNavigationItemSelected(any(MenuItem::class.java))).thenReturn(true)

        // select profile item
        bottomNavigationView.selectedItemId = R.id.navigation_profile
        verify(mockedListener, times(1))
            .onNavigationItemSelected(
                bottomNavigationView.menu.findItem(R.id.navigation_profile)
            )
        assertTrue(bottomNavigationView.menu.findItem(R.id.navigation_profile).isChecked)

        // select home item
        bottomNavigationView.selectedItemId = R.id.navigation_home
        verify(mockedListener, times(1))
            .onNavigationItemSelected(
                bottomNavigationView.menu.findItem(R.id.navigation_home)
            )
        assertTrue(bottomNavigationView.menu.findItem(R.id.navigation_home).isChecked)

        // select search item
        bottomNavigationView.selectedItemId = R.id.navigation_search
        verify(mockedListener, times(1))
            .onNavigationItemSelected(
                bottomNavigationView.menu.findItem(R.id.navigation_search)
            )
        assertTrue(bottomNavigationView.menu.findItem(R.id.navigation_search).isChecked)
    }

    @Test
    fun testViewedFragmentOnSelectedItem() {
        runBlocking {
            selectSearchItem()
            switchToSearchFragment()
        }
    }

    private suspend fun selectSearchItem() = withContext(Dispatchers.Main) {
        val mockedListener = mock(BottomNavigationView.OnNavigationItemSelectedListener::class.java)
        bottomNavigationView.setOnNavigationItemSelectedListener(mockedListener)
        `when`(mockedListener.onNavigationItemSelected(any(MenuItem::class.java))).thenReturn(true)
        // select search item
        bottomNavigationView.selectedItemId = R.id.navigation_search
        assertTrue(bottomNavigationView.menu.findItem(R.id.navigation_search).isChecked)
    }

    private suspend fun switchToSearchFragment() = withContext(Dispatchers.Default) {
        mainActivity.activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SearchFragment()).commit()
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))
    }
}
