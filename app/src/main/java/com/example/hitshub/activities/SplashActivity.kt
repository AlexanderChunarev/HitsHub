package com.example.hitshub.activities

import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hitshub.R
import com.example.hitshub.activities.MainActivity.Companion.WAKE_UP_MEDIA_PLAYER
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
        observe()
    }

    private fun getUserFromDataBase() {
        viewModel.setUser()
        viewModel.userInfoLiveData.observe(this@SplashActivity, Observer {
            val user = User(it.uId, it.name, it.email, it.avatarUrl).apply {
                isAnonymous = it.isAuth
            }
            if (intent.action == WAKE_UP_MEDIA_PLAYER) {
                navigateTo(MainActivity::class.java, user, ACTION_WAKE_UP)
            } else {
                navigateTo(MainActivity::class.java, user)
            }
            finish()
        })
    }

    override fun observeUserInfo(user: User) {
        when {
            user.isAnonymous -> {
                navigateTo(MainActivity::class.java, user)
            }
            !user.isAuth -> {
                navigateTo(RegisterActivity::class.java)
                finish()
            }
            else -> {
                getUserFromDataBase()
            }
        }
    }
}
