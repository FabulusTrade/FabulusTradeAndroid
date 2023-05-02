package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class AudienceData(
    val dateJoined: Date,
    val followersCount: Int?,
    val observerCount: Int?,
) : Parcelable