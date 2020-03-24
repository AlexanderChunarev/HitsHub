package com.example.hitshub.models

import java.io.Serializable

interface IRecyclerHorizontalModel : Serializable {
    val data: MutableList<*>
}
