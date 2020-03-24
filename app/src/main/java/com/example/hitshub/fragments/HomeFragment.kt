package com.example.hitshub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.hitshub.R
import com.example.hitshub.models.IAlbum
import com.example.hitshub.models.ITrack
import com.example.hitshub.models.VerticalModel

class HomeFragment : BaseMediaFragment() {
    override val arrayListVertical by lazy { mutableListOf<VerticalModel>() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onClickItem(response: ITrack) {
        val navController = NavHostFragment.findNavController(this)
        navController.navigate(R.id.player_fragment, Bundle().apply {
            putSerializable(PlayerFragment.RESPONSE_KEY, response)
        })
    }

    override fun onClickItem(response: IAlbum) {}
}
