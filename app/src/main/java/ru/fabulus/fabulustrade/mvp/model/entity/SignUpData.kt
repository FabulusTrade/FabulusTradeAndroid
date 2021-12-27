package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SignUpData(
    val username: String? = null,
    val password: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val patronymic: String? = null,
    val date_of_birth: String? = null,
    val gender: String? = null,
    val is_trader: Boolean? = null
) : Parcelable
