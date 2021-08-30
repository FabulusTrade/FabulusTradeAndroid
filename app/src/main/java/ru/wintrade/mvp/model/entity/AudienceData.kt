package ru.wintrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class AudienceData(
    val dateJoined: Date,
    val followersCount: Int?,
    val observerCount: Int?,
) : Parcelable