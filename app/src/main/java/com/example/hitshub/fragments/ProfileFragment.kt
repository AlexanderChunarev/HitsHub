package com.example.hitshub.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

import com.example.hitshub.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}
