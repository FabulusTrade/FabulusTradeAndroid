package ru.wintrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegistrationTraderData(
    val traderId: String,
    val isFastWay: Boolean
) : Parcelable
