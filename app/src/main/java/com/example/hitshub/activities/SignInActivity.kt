package com.example.hitshub.activities

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.hitshub.R
import com.example.hitshub.utils.GoogleSignInHelper
import com.example.hitshub.viewmodels.RegisterViewModel

class SignInActivity : BaseActivity() {
    private val googleSignInHelper by lazy { GoogleSignInHelper.getInstance() }
    override val viewModel by lazy { ViewModelProvider(this).get(RegisterViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }

    override fun observeAuthenticatedUser() {}

    override fun observeUserInfo() {}
}
