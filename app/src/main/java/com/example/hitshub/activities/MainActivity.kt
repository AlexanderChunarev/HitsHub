package com.example.hitshub.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hitshub.R
import com.example.hitshub.activities.BaseActivity.Companion.USER
import com.example.hitshub.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var googleSingInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initGoogleSingInClient()
        val user = intent.getSerializableExtra(USER) as User
        textView2.text = user.name
        textView3.text = user.email
    }

    private fun initGoogleSingInClient() {
        val googleOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSingInClient = GoogleSignIn.getClient(this, googleOptions)
    }
}
