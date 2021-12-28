package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class RequestTraderRegistrationInfo(
    @Expose
    val date_of_birth: String?,
    @Expose
    val first_name: String?,
    @Expose
    val last_name: String?,
    @Expose
    val patronymic: String?,
    @Expose
    val gender: String?,
    @Expose
    val is_trader: Boolean = true
)