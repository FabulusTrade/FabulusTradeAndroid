package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import ru.fabulus.fabulustrade.mvp.model.entity.api.RequestTraderRegistrationInfo

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