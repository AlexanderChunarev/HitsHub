package com.example.hitshub.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hitshub.R
import com.example.hitshub.fragments.MiniPlayerFragment
import com.example.hitshub.fragments.PlayerFragment
import com.example.hitshub.services.MediaPlayerService
import com.example.hitshub.viewmodels.DeezerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(DeezerViewModel::class.java)
    }
    private val serviceIntent by lazy { Intent(this, MediaPlayerService::class.java) }

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
            if (this == WAKE_UP_MEDIA_PLAYER) {
                supportFragmentManager.apply {
                    beginTransaction().replace(R.id.mini_player_container, MiniPlayerFragment())
                        .commit()
                    beginTransaction().replace(R.id.player_container, PlayerFragment()).commit()
                    (findViewById<MotionLayout>(R.id.motion_base)).transitionToEnd()
                }
                serviceIntent.action = WAKE_UP_MEDIA_PLAYER
                ContextCompat.startForegroundService(this@MainActivity, serviceIntent)
            }
        }

        viewModel.apply {
            getTopTracks()
            getTopAlbums()
        }
    }

    companion object {
        const val WAKE_UP_MEDIA_PLAYER = "open_player_from_notification"
    }
}
