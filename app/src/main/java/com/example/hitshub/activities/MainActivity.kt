package com.example.hitshub.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hitshub.R
import com.example.hitshub.fragments.HomeFragment
import com.example.hitshub.fragments.PlayerFragment
import com.example.hitshub.fragments.ProfileFragment
import com.example.hitshub.fragments.SearchFragment
import com.example.hitshub.models.Artist
import com.example.hitshub.models.Track
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var googleSingInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fakeTrack = Track(111, "adasda", "https://cdns-preview-d.dzcdn.net/stream/c-deda7fa9316d9e9e880d2c6207e92260-5.mp3", Artist(2, "", ""))
        if (savedInstanceState == null) {
            nav_view.visibility = View.GONE
            PlayerFragment.newInstance(fakeTrack).switch()
        }

        initGoogleSingInClient()
//        val user = intent.getSerializableExtra(USER) as User
//        textView2.text = user.uId
//        textView3.text = user.email

        nav_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> HomeFragment().switch()
                R.id.navigation_profile -> ProfileFragment().switch()
                R.id.navigation_search -> SearchFragment().switch()
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun initGoogleSingInClient() {
        val googleOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSingInClient = GoogleSignIn.getClient(this, googleOptions)
    }

    private fun Fragment.switch() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, this)
            .commit()
    }
}
