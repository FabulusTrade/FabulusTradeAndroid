package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultAddToBlacklist(
    val result: String
) : Parcelable