package com.example.hitshub

import android.view.MenuItem
import androidx.test.annotation.UiThreadTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.hitshub.activities.MainActivity
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
}
