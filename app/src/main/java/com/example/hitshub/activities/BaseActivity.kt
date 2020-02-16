package com.example.hitshub.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.hitshub.models.User
import com.example.hitshub.viewmodels.BaseViewModel

abstract class BaseActivity : AppCompatActivity() {
    protected abstract val viewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.showSpinner.observe(this, Observer {
            changeSpinnerDialogState(it == true)
        })
    }

    abstract fun observeAuthenticatedUser()

    abstract fun observeUserInfo()

    fun navigateTo(clazz: Class<*>, user: User? = null) {
        val intent = Intent(this, clazz)
        if (user != null) {
            intent.putExtra(USER, user)
        }
        startActivity(intent)
    }

    private fun changeSpinnerDialogState(isShouldShow: Boolean) {}

    companion object {
        const val USER = "user"
        const val SING_IN_CODE = 123
    }
}
