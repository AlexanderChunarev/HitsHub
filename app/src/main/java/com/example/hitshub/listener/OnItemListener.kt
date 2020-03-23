package com.example.hitshub.listener

import com.example.hitshub.models.IAlbum
import com.example.hitshub.models.ITrack

interface OnItemListener {
    fun onClickItem(response: ITrack)

    fun onClickItem(response: IAlbum)
}