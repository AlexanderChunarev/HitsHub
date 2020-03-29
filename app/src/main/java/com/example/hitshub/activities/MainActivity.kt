package com.example.hitshub.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hitshub.R
import com.example.hitshub.viewmodels.DeezerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(DeezerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        intent.action.apply {
            if (this == OPEN_PLAYER_FRAGMENT) {
                navController.navigate(R.id.player_fragment)
            }
        }
//        intent.action.apply {
//            if (this == OPEN_PLAYER_FRAGMENT) {
//                supportFragmentManager.apply {
//                    beginTransaction().replace(R.id.fragment_container, MiniPlayerFragment())
//                        .commit()
//                    beginTransaction().replace(R.id.fragment_container2, PlayerFragment()).commit()
//                    (findViewById<MotionLayout>(R.id.motion_item_activity)).transitionToEnd()
//                }
//            }
//        }

        viewModel.apply {
            getTopTracks()
            getTopAlbums()
        }
    }

    companion object {
        const val OPEN_PLAYER_FRAGMENT = "open_player_from_notification"
    }
}
