package com.example.hitshub.utils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.hitshub.R
import com.example.hitshub.activities.BaseActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSignInHelper {
    lateinit var googleSingInClient: GoogleSignInClient

    fun initGoogleSingInClient(context: Context) {
        val googleOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSingInClient = GoogleSignIn.getClient(context, googleOptions)
    }

    fun startSignInIntent(activity: AppCompatActivity) {
        val signInIntent = googleSingInClient.signInIntent
        activity.startActivityForResult(signInIntent, BaseActivity.SING_IN_CODE)
    }

    fun getSingedInAccountByIntent(data: Intent?): GoogleSignInAccount? {
        GoogleSignIn.getSignedInAccountFromIntent(data).run {
            try {
                return getResult(ApiException::class.java)
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) =
        GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)

    companion object {
        private var googleSignInAccount: GoogleSignInHelper? = null

        fun getInstance(): GoogleSignInHelper {
            if (googleSignInAccount == null) {
                googleSignInAccount = GoogleSignInHelper()
            }
            return googleSignInAccount!!
        }
    }
}
