package com.example.hitshub.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hitshub.models.FragmentState

class FragmentStateViewModel : ViewModel() {
    val fragmentStateLiveData by lazy { MutableLiveData<FragmentState>() }

    fun save(fragmentState: FragmentState) {
        fragmentStateLiveData.value = fragmentState
    }
}
