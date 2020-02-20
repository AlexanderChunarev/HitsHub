package com.example.hitshub.activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hitshub.R
import com.example.hitshub.models.User
import com.example.hitshub.utils.GoogleSignInHelper
import com.example.hitshub.viewmodels.RegisterViewModel
import com.google.firebase.auth.AuthCredential
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity() {
    private val googleSignInHelper by lazy { GoogleSignInHelper.getInstance() }
    override val viewModel by lazy { ViewModelProvider(this).get(RegisterViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        googleSignInHelper.initGoogleSingInClient(this)

        sign_in_with_google_button.setOnClickListener {
            googleSignInHelper.startSignInIntent(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SING_IN_CODE) {
            googleSignInHelper.getSingedInAccountByIntent(data).run {
                if (this != null) {
                    signInWithGoogleAuthCredential(googleSignInHelper.getGoogleAuthCredential(this))
                }
            }
        }
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        viewModel.singInWithGoogle(googleAuthCredential)
        observeAuthenticatedUser()
    }

    private fun createNewUser(user: User) {
        viewModel.createAccount(user)
        observeUserInfo()
    }

    override fun observeAuthenticatedUser() {
        viewModel.authenticatedUserLiveData.observe(this, Observer {
            if (it.isNew) {
                createNewUser(it)
                navigateTo(MainActivity::class.java, it)
                finishAffinity()
            } else {
                navigateTo(MainActivity::class.java, it)
            }
        })
    }

    override fun observeUserInfo() {
        viewModel.userInfoLiveData.observe(this, Observer {
        })
    }
}
