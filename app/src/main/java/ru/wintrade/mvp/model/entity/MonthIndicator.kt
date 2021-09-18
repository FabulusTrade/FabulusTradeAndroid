package ru.wintrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MonthIndicator(
    val year: Int?,
    val month: Int?,
    val actualProfitMonth: Double?
) : Parcelable