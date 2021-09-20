package ru.wintrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TraderRegistrationInfo(
    val dateOdBirth: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val patronymic: String = "",
    val gender: String = "",
) : Parcelable
