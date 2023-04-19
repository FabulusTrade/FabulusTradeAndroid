package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MonthIndicator(
    val year: Int?,
    val month: Int?,
    val actualProfitMonth: Double?,
    val colorActualItemMonth: ColorItem?
) : Parcelable