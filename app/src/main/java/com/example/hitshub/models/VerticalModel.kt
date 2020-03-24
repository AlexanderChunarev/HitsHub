package com.example.hitshub.models

import java.io.Serializable

data class VerticalModel(
    var title: String,
    var item: IRecyclerHorizontalModel
) : Serializable
