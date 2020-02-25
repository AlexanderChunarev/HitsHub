package com.example.hitshub.activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.hitshub.R
import com.example.hitshub.models.User
import com.example.hitshub.viewmodels.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override val viewModel by lazy {
        ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        create_account_button.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        anonymous_login_button.setOnClickListener {
            viewModel.signInAnonymously()
            observe()

        }
    }

    override fun observeUserInfo(user: User) {
        navigateTo(MainActivity::class.java, user)
        finishAffinity()
    }
}
