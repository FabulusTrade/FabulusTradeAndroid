package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.wintrade.mvp.model.entity.AudienceData
import java.util.*

data class ResponseAudienceData(
    @Expose
    @SerializedName("date_joined")
    override val dateJoined: Date,
    @Expose
    @SerializedName("followers_count")
    override val followersCount: Int,
    @Expose
    @SerializedName("observer_count")
    override val observerCount: Int
) : AudienceData
