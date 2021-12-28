package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class ResponseAudienceData(
    @Expose
    @SerializedName("date_joined")
    val dateJoined: Date,
    @Expose
    @SerializedName("followers_count")
    val followersCount: Int?,
    @Expose
    @SerializedName("observer_count")
    val observerCount: Int?
)
