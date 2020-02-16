package com.example.hitshub.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hitshub.R
import com.example.hitshub.models.User
import com.example.hitshub.utils.GoogleSignInHelper
import com.example.hitshub.viewmodels.RegisterViewModel
import com.google.firebase.auth.AuthCredential
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    private val googleSignInHelper by lazy { GoogleSignInHelper.getInstance() }
    override val viewModel by lazy { ViewModelProvider(this).get(RegisterViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleSignInHelper.initGoogleSingInClient(this)
        setContentView(R.layout.activity_sign_up)

        sign_up_with_google_button.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInHelper.googleSingInClient.signInIntent
        startActivityForResult(signInIntent, SING_IN_CODE)
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
        this.viewModel.apply {
            singInWithGoogle(googleAuthCredential)
            authenticatedUserLiveData.observe(this@SignUpActivity, Observer {
                if (it.isNew) {
                    createNewUser(it)
                    navigateTo(MainActivity::class.java, it)
                    finishAffinity()
                } else {
                    toastMessage(it.name)
                }
            })
        }
    }

    private fun createNewUser(user: User) {
        this.viewModel.apply {
            createAccount(user)
            createdUserLiveData.observe(this@SignUpActivity, Observer {
                if (it.isCreated) {
                    toastMessage(it.name)
                }
            })
        }
    }

    private fun toastMessage(name: String) {
        Toast.makeText(
            this,
            "Hi $name!\nYour account was already created.",
            Toast.LENGTH_LONG
        ).show()
    }
}
