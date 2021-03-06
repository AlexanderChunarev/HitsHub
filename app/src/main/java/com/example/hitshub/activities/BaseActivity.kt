package com.example.hitshub.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.hitshub.activities.MainActivity.Companion.WAKE_UP_MEDIA_PLAYER
import com.example.hitshub.extentions.changeSpinnerDialogState
import com.example.hitshub.models.User
import com.example.hitshub.utils.Constants.EMPTY_STRING
import com.example.hitshub.utils.MessageUtils
import com.example.hitshub.viewmodels.BaseViewModel

abstract class BaseActivity : AppCompatActivity() {
    protected abstract val viewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.showSpinner.observe(this, Observer {
            supportFragmentManager.changeSpinnerDialogState(it)
        })

        viewModel.message.observe(this, Observer {
            when (it) {
                is MessageUtils.ErrorMessage -> Toast.makeText(
                    this,
                    it.errorString,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    abstract fun observeUserInfo(user: User)

    fun observe() {
        viewModel.authenticatedUserLiveData.observe(this, Observer {
            observeUserInfo(it)
        })
    }

    fun navigateTo(clazz: Class<*>, user: User? = null, action: String = EMPTY_STRING) {
        val intent = Intent(this, clazz)
        if (user != null) {
            intent.putExtra(USER, user)
        }
        if (action.isNotEmpty()) {
            intent.putExtra(WAKE_UP_MEDIA_PLAYER, action)
        }
        startActivity(intent)
    }

    companion object {
        const val USER = "user"
        const val SING_IN_CODE = 123
        const val ACTION_WAKE_UP = "wake_up"
    }
}
