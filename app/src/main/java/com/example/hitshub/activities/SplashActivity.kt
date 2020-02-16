package com.example.hitshub.activities

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hitshub.models.User
import com.example.hitshub.viewmodels.SplashViewModel

class SplashActivity : BaseActivity() {
    override val viewModel by lazy {
        ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkUserAuntification()
    }

    private fun checkUserAuntification() {
        viewModel.setUserAuntificationInfo()
        observeAuthenticatedUser()
    }

    private fun getUserFromDataBase(id: String) {
        viewModel.setUserByUid(id)
        observeUserInfo()
    }

    override fun observeAuthenticatedUser() {
        viewModel.authenticatedUserLiveData.observe(this@SplashActivity, Observer {
            if (!it.isAuth) {
                navigateTo(RegisterActivity::class.java)
                finish()
            } else {
                getUserFromDataBase(it.uId)
            }
        })
    }

    override fun observeUserInfo() {
        viewModel.userInfoLiveData.observe(this@SplashActivity, Observer {
            val user = User(it.uId, it.name, it.email, it.avatarUrl)
            navigateTo(MainActivity::class.java, user)
            finish()
        })
    }
}
