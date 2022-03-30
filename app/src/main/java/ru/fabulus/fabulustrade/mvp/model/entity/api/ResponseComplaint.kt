package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseComplaint(
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("complaint")
    val text: String
)