package com.example.fitpeo.data.repository.model

import com.google.gson.annotations.SerializedName

data class Content(
    val name: String,
    @SerializedName("poster-image")
    val posterImage: String,
)
