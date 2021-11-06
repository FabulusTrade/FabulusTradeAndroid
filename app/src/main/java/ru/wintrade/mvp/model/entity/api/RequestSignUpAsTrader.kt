package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class RequestSignUpAsTrader(
    @Expose
    val username: String,
    @Expose
    val password: String,
    @Expose
    val email: String,
    @Expose
    val phone: String,
    @Expose
    val first_name: String,
    @Expose
    val last_name: String,
    @Expose
    val patronymic: String,
    @Expose
    val date_of_birth: String,
    @Expose
    val gender: String,
    @Expose
    val is_trader: Boolean = true
)