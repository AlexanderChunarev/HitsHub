package com.example.hitshub.activities

import android.content.Intent
import android.os.Bundle
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

        sing_in_with_email_button.setOnClickListener {
            signInWithEmailAndPassword(
                email_editText.text.toString(),
                password_editText.text.toString()
            )
        }

        sign_up_with_email_button.setOnClickListener {
            signUpWithEmailAndPassword(
                email_editText.text.toString(),
                password_editText.text.toString()
            )
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
        observe()
    }

    private fun signUpWithEmailAndPassword(email: String, password: String) {
        viewModel.signUpWithEmailAndPassword(email, password)
        observe()
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        viewModel.signInWithEmailAndPassword(email, password)
        observe()
    }

    private fun saveUserToFireStore(user: User) {
        viewModel.saveUserToFireStore(user)
    }

    override fun observeUserInfo(user: User) {
        if (user.isNew) {
            saveUserToFireStore(user)
            navigateTo(MainActivity::class.java, user)
            finishAffinity()
        } else {
            navigateTo(MainActivity::class.java, user)
            finishAffinity()
        }
    }
}
