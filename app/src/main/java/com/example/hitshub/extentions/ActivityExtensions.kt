package com.example.hitshub.extentions

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.hitshub.R
import com.google.android.material.bottomnavigation.BottomNavigationView

fun FragmentActivity.showSupportActionBar(state: Int) {
    (this as AppCompatActivity).run {
        when (state) {
            View.GONE -> this.supportActionBar!!.hide()
            View.VISIBLE -> this.supportActionBar!!.show()
        }
    }
}

fun FragmentActivity.setBottomNavigationViewVisibility(state: Int) {
    this.findViewById<BottomNavigationView>(R.id.nav_view).visibility = state
}
