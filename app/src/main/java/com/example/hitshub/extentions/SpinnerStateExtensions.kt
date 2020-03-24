package com.example.hitshub.extentions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.hitshub.fragments.LoadingStateFragment

fun FragmentManager.changeSpinnerDialogState(isShouldShow: Boolean) {
    if (isShouldShow) {
        LoadingStateFragment.newInstance().show(this,
            LoadingStateFragment.LOADING_FRAGMENT_TAG
        )
    } else {
        (this.findFragmentByTag(LoadingStateFragment.LOADING_FRAGMENT_TAG) as? DialogFragment)?.dismiss()
    }
}
