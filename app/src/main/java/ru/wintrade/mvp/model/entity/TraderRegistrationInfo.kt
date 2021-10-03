package ru.wintrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import ru.wintrade.mvp.model.entity.api.RequestTraderRegistrationInfo

@Parcelize
data class TraderRegistrationInfo(
    val dateOfBirth: String?,
    val firstName: String?,
    val lastName: String?,
    val patronymic: String?,
    val gender: @RawValue Gender,
) : Parcelable {

    fun toRequest(): RequestTraderRegistrationInfo =
        RequestTraderRegistrationInfo(
            date_of_birth = dateOfBirth,
            first_name = firstName,
            last_name = lastName,
            patronymic = patronymic,
            gender = gender.char
        )
}